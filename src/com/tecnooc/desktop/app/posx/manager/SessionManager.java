package com.tecnooc.desktop.app.posx.manager;

import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.DocSequenceDto;
import com.tecnooc.desktop.app.posx.dto.StoreDto;
import com.tecnooc.desktop.app.posx.dto.TerminalDto;
import com.tecnooc.desktop.app.posx.dto.UserDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.service.ApplicationPreferenceValueService;
import com.tecnooc.desktop.app.posx.service.DocSequenceService;
import com.tecnooc.desktop.app.posx.service.StoreService;
import com.tecnooc.desktop.app.posx.service.TerminalService;
import com.tecnooc.desktop.app.posx.service.UserService;
import com.tecnooc.desktop.app.posx.service.impl.ZoutServiceimpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class SessionManager implements InitializingBean {
    private StoreDto store;
    private TerminalDto terminal;
    private UserDto user;
    private DocSequenceDto docSequence;
    private String preferedLanguage;
    private String weighingItemPrefix;
    private int weighingItemCodeLength;
    private int weighingItemQuantityLength;
    private int weighingItemPriceLength;
    private String nonWeighingItemPrefix;
    private int nonWeighingItemCodeLength;
    private int nonWeighingItemPriceLength;
    private String productExpiryAction;
    
    private UserPermissionsDto userPermissions;
    
    @Autowired private UserService userService;
    @Autowired private StoreService storeService;
    @Autowired private TerminalService terminalService;
    @Autowired private DocSequenceService docSequenceService;
    @Autowired private ApplicationPreferenceValueService applicationPreferenceValueService;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        store = storeService.getStore();
        terminal = terminalService.getTerminal();
        
        if (store == null || terminal == null) {
            Util.exitWithErrorMessage("No store or terminal information found.");
        }
        
        docSequence = docSequenceService.find(store, terminal);
        preferedLanguage = applicationPreferenceValueService.getPreferedLanguage();
        weighingItemPrefix = applicationPreferenceValueService.getWeighingItemPrefix();
        weighingItemCodeLength = applicationPreferenceValueService.getWeighingItemCodeLength();
        weighingItemQuantityLength = applicationPreferenceValueService.getWeighingItemQuantityLength();
        nonWeighingItemPrefix = applicationPreferenceValueService.getNonWeighingItemPrefix();
        nonWeighingItemCodeLength = applicationPreferenceValueService.getNonWeighingItemCodeLength();
        nonWeighingItemPriceLength = applicationPreferenceValueService.getNonWeighingItemPriceLength();
        productExpiryAction = applicationPreferenceValueService.getProductExpiryAction();
    }    
    
    public boolean login(String username, String password) {
        user = userService.login(username, password);
        if (user == null)
            return false;
        
        return true;
    }
    
    public void logout() {
        store       = null;
        terminal    = null;
        user        = null;
        docSequence = null;
    }
    
    public Integer nextSequenceValue() {
        ensureUniqueInvcNo();
        return docSequence.nextValue();
    }
    
    public void updateSequenceValue() {
        docSequence.increment();
        docSequenceService.update(docSequence);
    }
    
    public int getSubsidiaryNumber() {
        return store.getSbusidiaryNumber();
    }

    public StoreDto getStore() {
        return store;
    }

    public TerminalDto getTerminal() {
        return terminal;
    }

    public UserDto getUser() {
        return user;
    }

    public String getPreferedLanguage() {
        return preferedLanguage;
    }

    public String getWeighingItemPrefix() {
        return weighingItemPrefix;
    }

    public int getWeighingItemCodeLength() {
        return weighingItemCodeLength;
    }

    public int getWeighingItemQuantityLength() {
        return weighingItemQuantityLength;
    }

    public int getWeighingItemPriceLength() {
        return weighingItemPriceLength;
    }

    public String getNonWeighingItemPrefix() {
        return nonWeighingItemPrefix;
    }

    public int getNonWeighingItemCodeLength() {
        return nonWeighingItemCodeLength;
    }

    public int getNonWeighingItemPriceLength() {
        return nonWeighingItemPriceLength;
    }

    public String getProductExpiryAction() {
        return productExpiryAction;
    }
    
    public void readPermissions() {
        // DONE LIKE THIS FOR FAST DEVELOPMENT
        try {
            String query = "SELECT perm_id FROM "
                    + "pos_user_group_map ugm JOIN `pos_user_group_permission` ugp "
                    + "ON (ugm.`user_group_id`=ugp.`user_group_id`) "
                    + "WHERE ugm.user_id=? AND ugp.`perm_allowed`=1";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posx?user=tecnooc&password=qo7^vx$pfj9");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getEntity().getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            userPermissions = new UserPermissionsDto();
            while (resultSet.next()) {
                userPermissions.addPermission(resultSet.getInt("perm_id"));
            }
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ZoutServiceimpl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    public UserPermissionsDto getUserPermissions() {
        return userPermissions;
    }
    
    private void ensureUniqueInvcNo() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posx?user=tecnooc&password=qo7^vx$pfj9");
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM pos_invoice WHERE invc_no=" + docSequence.nextValue());
            if (res.next()) {
                res.close();
                stmt.close();

                stmt = connection.createStatement();
                res = stmt.executeQuery("SELECT MAX(invc_no) AS max_invc_no FROM pos_invoice");
                res.next();
                docSequence.getEntity().setDocSeqNextValue(res.getInt("max_invc_no") + 1);
                docSequenceService.update(docSequence);
            }
            res.close();
            stmt.close();

            connection.close();
        } catch (SQLException ex) {

        }
    }
}
