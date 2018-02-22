/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class ReceiptListViewController extends AbstractViewController implements InsertableView {    
    @FXML private ListView itemList;    
    @Autowired private ReceiptManager receiptManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        itemList.setItems(receiptManager.heldReceiptsProperty());
        
        itemList.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //System.out.println("KeyEvent: " + event);
                if (event.getCode() == KeyCode.ENTER) {
                    //System.out.println("Adding Item.");
                    addItem(null);
                }
            }
        });
    }
    
    @FXML
    public void addItem(MouseEvent event) {
        final ReceiptDto receipt = (ReceiptDto) itemList.getSelectionModel().getSelectedItem();
        if (receiptManager.isLoaded(receipt)) {
            return;
        }
        
        if (receipt != null) {
            if (!receiptManager.isCurrentReceiptEmpty()) {
                Dialog.showMessageDialog(
                        getView().getScene(), 
                        "Load Receipt", 
                        "This action will load the selected receipt and current one will be held. Are you sure?", 
                        Dialog.DialogType.OK_CANCEL,
                        new DialogListener() {
                    @Override
                    public void dialogDismissed(Dialog.ButtonType pressedButton) {
                        if (pressedButton == Dialog.ButtonType.OK) {
                            receiptManager.loadReceipt(receipt);
                        }
                    }
                });
            } else {
                receiptManager.loadReceipt(receipt);
            }
        }
    }
    
    public void focus() {
        
    }

    @Override
    public void beforeInsert() { }

    @Override
    public void afterInsert() { 
        itemList.requestFocus();
    }
}
