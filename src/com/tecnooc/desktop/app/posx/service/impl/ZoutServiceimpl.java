package com.tecnooc.desktop.app.posx.service.impl;

import com.mysql.jdbc.Driver;
import com.tecnooc.desktop.app.posx.dto.ZoutCurrencyDto;
import com.tecnooc.desktop.app.posx.dto.ZoutDto;
import com.tecnooc.desktop.app.posx.dto.ZoutTenderDto;
import com.tecnooc.desktop.app.posx.model.Zout;
import com.tecnooc.desktop.app.posx.model.ZoutCurrency;
import com.tecnooc.desktop.app.posx.model.ZoutTender;
import com.tecnooc.desktop.app.posx.repository.ZoutCurrencyRepository;
import com.tecnooc.desktop.app.posx.repository.ZoutRepository;
import com.tecnooc.desktop.app.posx.repository.ZoutTenderRepository;
import com.tecnooc.desktop.app.posx.service.ZoutService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jomit
 */
@Service
public class ZoutServiceimpl implements ZoutService {
    @Autowired private ZoutRepository repository;
    @Autowired private ZoutCurrencyRepository currencyRepository;
    @Autowired private ZoutTenderRepository tenderRepository;

    @Override
    public ZoutDto findLastReport() {
        Zout lastReport = repository.findLastReport();
        lastReport.setZoutCurrencyList(currencyRepository.findByZout(lastReport));
        return lastReport == null ? null : new ZoutDto(lastReport);
    }    
    
    @Override
    public ZoutDto findLastReportWithoutCurrencyList() {
        Zout lastReport = repository.findLastReport();
        return lastReport == null ? null : new ZoutDto(lastReport);
    }

    @Override
    public void saveNew(ZoutDto zout)
    {
        repository.save(zout.getEntity());
    }

    @Override
    public void save(ZoutDto zout) {
        long start = new Date().getTime();
        System.out.println("--- Start Save ---");
        
        try {
            DriverManager.registerDriver(new Driver());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posx?user=tecnooc&password=qo7^vx$pfj9");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE pos_zout SET user_id=?, modified_date=?, period_end=?, deposit_amt=?, drawer_leave_amt=?, tender_total_close=?, over_short_amt=? WHERE zout_sid=?");

            preparedStatement.setLong(1, zout.getUser().getUserId().intValue());
            preparedStatement.setDate(2, new java.sql.Date(zout.getModifiedDate().getTime()));
            if (zout.getPeriodEnd() != null) {
                preparedStatement.setDate(3, new java.sql.Date(zout.getPeriodEnd().getTime()));
            } else {
                preparedStatement.setDate(3, null);
            }
            preparedStatement.setBigDecimal(4, zout.getDepositAmt());
            preparedStatement.setBigDecimal(5, zout.getDrawerLeaveAmt());
            preparedStatement.setBigDecimal(6, zout.getTenderTotalClose());
            preparedStatement.setBigDecimal(7, zout.getOverShortAmt());
            preparedStatement.setLong(8, ((Zout) zout.getEntity()).getZoutSid().longValue());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ZoutServiceimpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        ZoutDto lastReport = findLastReport();
        try {
            DriverManager.registerDriver(new Driver());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posx?user=tecnooc&password=qo7^vx$pfj9");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pos_zout_currency(zout_sid, crrency_denom_id, multiplier, amount, actual_amount, amount_diff) VALUES(?,?,?,?,?,?)");
            for (ZoutCurrencyDto currency : zout.getZoutCurrencyList()) {
                ZoutCurrency entity = (ZoutCurrency) currency.getEntity();
                preparedStatement.setLong(1, ((Zout) lastReport.getEntity()).getZoutSid().longValue());
                preparedStatement.setLong(2, entity.getZoutCurrencyPK().getCrrencyDenomId());
                preparedStatement.setInt(3, entity.getMultiplier());
                preparedStatement.setBigDecimal(4, entity.getAmount());
                preparedStatement.setBigDecimal(5, entity.getActualAmount());
                preparedStatement.setBigDecimal(6, entity.getAmountDiff());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("INSERT INTO pos_zout_tender(zout_sid, amount, actual_amount, amount_diff) VALUES(?,?,?,?)");
            for (ZoutTenderDto tender : zout.getZoutTenderList()) {
                ZoutTender entity = (ZoutTender) tender.getEntity();
                preparedStatement.setLong(1, ((Zout) lastReport.getEntity()).getZoutSid().longValue());
                preparedStatement.setBigDecimal(2, entity.getAmount());
                preparedStatement.setBigDecimal(3, entity.getActualAmount());
                preparedStatement.setBigDecimal(4, entity.getAmountDiff());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();

            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ZoutServiceimpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("--- Finish Save ---");
        long finish = new Date().getTime();
        System.out.println("Time Taken: " + (finish - start));
    }

    @Override
    public ZoutDto findById(Long id) {
        Zout entity = repository.findOne(id);
        entity.setZoutCurrencyList(currencyRepository.findByZout(entity));
        return new ZoutDto(entity);
    }

    @Override
    public void updateInvoiceSid(ZoutDto zout) {
        long start = new Date().getTime();
        System.out.println("--- Start Update ---"); 
        
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posx?user=tecnooc&password=qo7^vx$pfj9");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE pos_zout SET open_invc_sid=?, close_invc_sid=? WHERE zout_sid=?");
            preparedStatement.setLong(1, zout.getOpenInvcSid().getInvcSid());
            preparedStatement.setLong(2, zout.getCloseInvcSid().getInvcSid());
            preparedStatement.setLong(3, zout.getEntity().getZoutSid());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ZoutServiceimpl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        System.out.println("--- Finish Update ---");
        long finish = new Date().getTime();
        System.out.println("Time Taken: " + (finish - start));
    }
}

