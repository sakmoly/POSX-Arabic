package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.DialogView;
import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import com.tecnooc.desktop.app.posx.service.CustomerService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
    
/**
 *
 * @author jomit
 */
@Component
public class SearchCustomerViewController extends AbstractViewController implements DialogView, DialogListener {
    @FXML
    private TextField lookupTextField;
    @FXML
    private ListView customerList;
    
    @Autowired
    private CustomerService service;
    private ObservableList<CustomerDto> customers;    
    private CustomerDto selectedCustomer;    
    private boolean keytypeSuggest;
    private DialogListener listener;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet(); 
        onInit();
    }
    
    public CustomerDto getSelectedCustomer() {
        return selectedCustomer;
    }
           
    private void lookupCustomer() {
        if (keytypeSuggest) {
            customers.clear();
            if (lookupTextField.getText().isEmpty()) {
                keytypeSuggest = false;
            } else {
                try {
                    customers.addAll(service.lookup(lookupTextField.getText()));
                } catch (Exception ex) {
                    Util.showInternalExceptionMessage(ex);
                }
            }
        }
    }
    
    @FXML
    private void keyTyped(KeyEvent ke) {
        char ch = ke.getCharacter().charAt(0);
        if (ch == '\n' || ch == '\r') {
            keytypeSuggest = true;
        }
        
        lookupCustomer();
        
        if (ch == '\n' || ch == '\r') {
            if (doOK()) {
                Dialog.hide(Dialog.ButtonType.OK);
            } else if (customers.size() > 0) {
                customerList.requestFocus();
            }
        }
    }

    public void onInit()  {        
        customers = FXCollections.observableArrayList();
        customerList.setItems(customers);
        
//        customerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                if (doOK()) {
//                    Dialog.hide(Dialog.ButtonType.OK);
//                }
//            }
//        });
        
        customerList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (doOK()) {
                    Dialog.hide(Dialog.ButtonType.OK);
                }
            }
        });
        
        customerList.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && doOK()) {
                    Dialog.hide(Dialog.ButtonType.OK);
                }
            }
        });
    }

    @Override
    public void onBeforeShow() {
        keytypeSuggest = false;
        customers.clear();
        lookupTextField.setText("");
        lookupTextField.requestFocus();
    }

    public boolean doOK() {    
        if (customers.size() == 1) {
            selectedCustomer = customers.get(0);
            return true;
        } else {
            selectedCustomer = (CustomerDto) customerList.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean doCancel() {
        selectedCustomer = null;
        return true;
    }

    @Override
    public boolean onBeforeHide(Dialog.ButtonType button) {
        if (button == Dialog.ButtonType.CANCEL) {
            return true;
        }
        
        CustomerDto selected = (CustomerDto) customerList.getSelectionModel().getSelectedItem();
        if (selected != null || customers.size() == 1) {
            return true;
        }
        
        return false;
    }
    
    public void show(Scene currentScene, DialogListener listener) {
        this.listener = listener;
        Dialog.showCustomDialog(currentScene, "Select Customer", this, Dialog.DialogType.OK_CANCEL, this, false, true);
    }

    @Override
    public void dialogDismissed(Dialog.ButtonType pressedButton) {
        if (pressedButton == Dialog.ButtonType.OK) {
            doOK();
        } else {
            doCancel();
        }
        
        listener.dialogDismissed(pressedButton);
    }
}
