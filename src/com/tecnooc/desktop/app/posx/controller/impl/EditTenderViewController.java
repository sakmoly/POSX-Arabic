/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.controller.util.Shortcut;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutActionListener;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class EditTenderViewController extends AbstractViewController implements InsertableView {
    @FXML private Label tenderNameLabel;
    @FXML private Label amountLabel;
    @Autowired private ReceiptManager receiptManager;
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private ReceiptViewController receiptViewController;
    @Autowired private SessionManager sessionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        
        shortcutManager.setShortcutListener(Shortcut.TENDER_DELETE, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (receiptManager.getSelectedTender() != null) {
                    removeTender(null);
                }
            }
        });
    }
    
    @FXML
    private void removeTender(ActionEvent ae) {
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.DELETE_TENDER)) {
            AuthorizationDialog.show();
            return;
        }
        
        receiptManager.removeSelectedTender();
        if (receiptManager.currentItemsProperty().isEmpty()) {
            receiptViewController.showSearchItemView(null);
        }
        receiptViewController.showTenderView(null);
    }
    
    @FXML
    private void removeAllTenders(ActionEvent ae) {
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.DELETE_TENDER)) {
            AuthorizationDialog.show();
            return;
        }
        
        receiptManager.removeAllTenders();
        if (receiptManager.currentItemsProperty().isEmpty()) {
            receiptViewController.showSearchItemView(null);
        }
        receiptViewController.showTenderView(null);
    }

    @Override
    public void beforeInsert() {
        ReceiptTenderDto selectedTender = receiptManager.getSelectedTender();
        if (selectedTender == null) {
            tenderNameLabel.setText("I \u2764 U");
            amountLabel.setText("\u2763  \u27B3");
        } else {
            tenderNameLabel.setText(selectedTender.tenderNameProperty().get());
            amountLabel.setText(selectedTender.getAmount().toString());
        }
    }

    @Override
    public void afterInsert() {}
}
