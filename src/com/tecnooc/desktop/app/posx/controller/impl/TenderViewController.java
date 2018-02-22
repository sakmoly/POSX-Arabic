package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.controller.util.Numpad;
import com.tecnooc.desktop.app.posx.controller.util.NumpadAction;
import com.tecnooc.desktop.app.posx.controller.util.Shortcut;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import com.tecnooc.desktop.app.posx.model.Tender;
import com.tecnooc.desktop.app.posx.service.TenderService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class TenderViewController extends AbstractViewController implements InsertableView { 
    @FXML private Label customerLabel;
    @FXML private Label storeBalanceLabel;
    @FXML private Label creditUsedLabel;
    
    private Numpad numpad;
    @Autowired private ReceiptManager receiptManager;
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private TenderCreditCardViewController creditCardViewController;
    @Autowired private TenderService tenderService;
    @Autowired private SessionManager sessionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        initNumpad();
        
        CustomerDto customer = receiptManager.getCurrentCustomer();
        customerLabel.textProperty().bind(customer.idNameProperty());
        creditUsedLabel.setText(customer.getCreditUsed().toString());
        storeBalanceLabel.setText(customer.getStoreCreditAvailable().toString());
        
        receiptManager.currentCustomerProperty().addListener(new ChangeListener<CustomerDto>() {
            @Override
            public void changed(ObservableValue<? extends CustomerDto> observable, CustomerDto oldValue, final CustomerDto newCustomer) {
                customerLabel.textProperty().bind(newCustomer.idNameProperty());
                creditUsedLabel.setText(newCustomer.getCreditUsed().toString());
                storeBalanceLabel.setText(newCustomer.getStoreCreditAvailable().toString());
                newCustomer.creditUsedProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        creditUsedLabel.setText(newCustomer.getCreditUsed().toString());
                    }
                });
                newCustomer.storeCreditProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        storeBalanceLabel.setText(newCustomer.getStoreCreditAvailable().toString());
                    }
                });
            }
        });
    }

    private void initNumpad() {
        numpad = new Numpad(getView(), false, shortcutManager);
        AnchorPane view = (AnchorPane) getView();

        view.getChildren().add(numpad);

        numpad.setPrefWidth(352.0);
        numpad.setPrefHeight(520.0);
        AnchorPane.setTopAnchor(numpad, 108.0);
        AnchorPane.setLeftAnchor(numpad, 0.0);
        AnchorPane.setBottomAnchor(numpad, 0.0);
        AnchorPane.setRightAnchor(numpad, 0.0);
        
        int count = 0;
        List<Tender> activeTenders = tenderService.findAllActiveTenders();
        if (activeTenders.isEmpty()) {
            Util.showErrorMessage("No active tenders found.");
        }
        
        //NumpadAction[] actions = {cashAction, creditCardAction, storeCreditAction, creditSaleAction};
        boolean[] needValues = {true, true, false, true};
        int[] shortcutIds = {Shortcut.TENDER_CASH, Shortcut.TENDER_BANK_CARD, Shortcut.TENDER_STORE_CREDIT, Shortcut.TENDER_CREDIT_SALE};
        for (Tender tender : activeTenders) {
            int tenderType = tender.getTenderType();
            NumpadTenderAction action = null;
            switch (tenderType) {
                case 0:
                    action = new CashAction();
                    action.setReturnReceiptInclude(tender.getReturnInvoiceInclude());
                    break;
                case 1:
                    action = new CreditCardAction();
                    action.setReturnReceiptInclude(tender.getReturnInvoiceInclude());
                    break;
                case 2:
                    action = new StoreCreditAction();
                    action.setReturnReceiptInclude(tender.getReturnInvoiceInclude());
                    break;
                case 3:
                    action = new CreditSaleAction();
                    action.setReturnReceiptInclude(tender.getReturnInvoiceInclude());
                    break;
            }
            
            if (tenderType < 4) {
                if (count >= 3) {
                    numpad.setExtendedAction(count - 3, tender.getTenderShortLbl(), needValues[tenderType], action, shortcutIds[tenderType]);
                } else {
                    numpad.setPrimaryAction(count, tender.getTenderShortLbl(), needValues[tenderType], action, shortcutIds[tenderType]);
                }
            }
            
            count++;
        }
    }

    @Override
    public void beforeInsert() { }
    @Override
    public void afterInsert() {
        numpad.reset();
        numpad.focusInputField();
    }
    
    private abstract class NumpadTenderAction implements NumpadAction {
        protected boolean returnInvoiceInclude;
        
        public void setReturnReceiptInclude(boolean include) {
            this.returnInvoiceInclude = include;
        }
    }
    
    private class CashAction extends NumpadTenderAction {
        @Override
        public void handle(Numpad.NumpadValue value) {
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ACCESS_TENDER_CASH)) {
                AuthorizationDialog.show();
                return;
            }
            
            if (receiptManager.totalProperty().get().compareTo(BigDecimal.ZERO) <= 0) {
                if (receiptManager.hasReturnedItem()) {
                    BigDecimal cash = new BigDecimal(value.getValue());
                    BigDecimal change = receiptManager.totalProperty().get().abs();
                    BigDecimal given = change.compareTo(cash) > 0 ? cash : change; 

                    ReceiptTenderDto tender = new ReceiptTenderDto();
                    tender.setTenderType(ReceiptTenderDto.TENDER_CASH);
                    tender.setAmount(given.negate().setScale(2, RoundingMode.HALF_EVEN));
                    tender.setGivenAmount(given.setScale(2, RoundingMode.HALF_EVEN));
                    receiptManager.addTender(tender);
                    
                    return;
                }
                
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "Over tender is not allowed.");
                return;
            }

            ReceiptTenderDto tender = new ReceiptTenderDto();
            tender.setTenderType(ReceiptTenderDto.TENDER_CASH);
            tender.setAmount(new BigDecimal(value.getValue()).setScale(2, RoundingMode.HALF_EVEN));
            receiptManager.addTender(tender);
        }
    };
    
    private class CreditCardAction extends NumpadTenderAction {
        @Override
        public void handle(Numpad.NumpadValue value) {
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ACCESS_TENDER_BANK_CARD)) {
                AuthorizationDialog.show();
                return;
            }

            if (receiptManager.totalProperty().get().compareTo(BigDecimal.ZERO) <= 0) {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "Over tender is not allowed.");

                return;
            }

            final BigDecimal amount = new BigDecimal(value.getValue());
            if (amount.compareTo(receiptManager.totalProperty().get()) > 0) {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "You cannot tender more than what is due with credit/debit card.");
                return;
            }

            creditCardViewController.show(getView().getScene(), new DialogListener() {
                @Override
                public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                    if (buttonPressed == Dialog.ButtonType.OK) {
                        ReceiptTenderDto tender = creditCardViewController.getCreditCardInfo();

                        if (tender != null) {
                            tender.setTenderType(ReceiptTenderDto.TENDER_CREDIT_CARD);
                            tender.setAmount(amount.setScale(2, RoundingMode.HALF_EVEN));
                            receiptManager.addTender(tender);
                        }
                    }
                }
            });
        }
    };
    
    private class StoreCreditAction extends NumpadTenderAction {
        @Override
        public void handle(Numpad.NumpadValue value) {            
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.REDEEM_STORE_CREDIT)) {
                AuthorizationDialog.show();
                return;
            }

            if (receiptManager.getCurrentCustomer().isDefaultCustomer()) {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "You have to select a customer first to apply store credit.");
            } else {
                receiptManager.useStoreCredit(returnInvoiceInclude);
            }
        }
    };
    
    private class CreditSaleAction extends NumpadTenderAction {
        @Override
        public void handle(Numpad.NumpadValue value) {            
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ACCESS_TENDER_CREDIT_SALE)) {
                AuthorizationDialog.show();
                return;
            }
            
            if (receiptManager.getCurrentCustomer().isDefaultCustomer()) {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "You have to select a customer first to apply credit sale.");
                return;
            }

            int credit = (int) value.getValue();
            int creditRemaining = receiptManager.getCurrentCustomer().getCreditRemaining();
            int total = receiptManager.getIntegerTotal();
            
            if (total < 0 && returnInvoiceInclude) {
                int change = -total;
                int given = Math.min(change, credit);

                receiptManager.useCreditSale(-given);

                return;
            }
            
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.CREDIT_SALE_EXCEED_CREDIT_LIMIT)) {
                if (credit > creditRemaining) {
                    Dialog.showMessageDialog(getView().getScene(),
                            "Invalid Credit",
                            "You cannot use more than remaining credit(" + creditRemaining + ").");
                    return;
                }
            }

            if (credit > total) {
                Dialog.showMessageDialog(getView().getScene(),
                        "Invalid Credit",
                        "You cannot use more than total.");
                return;
            }

            receiptManager.useCreditSale(credit);
        }
    };
}
