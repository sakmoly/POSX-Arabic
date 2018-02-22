package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.PosxApp;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.impl.ReceiptViewController;
import com.tecnooc.desktop.app.posx.dto.DtoException;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;

/**
 *
 * @author jomit
 */
public class BasicNumpad extends Numpad {
    private ReceiptManager receiptManager;
    private SessionManager sessionManager;
    private ReceiptViewController receiptViewController;
    
    public BasicNumpad(Node node, ReceiptViewController receiptViewController, ReceiptManager receiptManager, ShortcutManager shortcutManager, SessionManager sessionManager) {
        super(node, true, shortcutManager);
        
        this.receiptViewController = receiptViewController;
        this.receiptManager = receiptManager;
        this.sessionManager = sessionManager;
        
        init();
    }

    private void init() {
        setPrimaryAction(0, "Quantity", true, new NumpadAction() {
            @Override
            public void handle(Numpad.NumpadValue value) {
                ReceiptItemDto selectedItem = receiptManager.getSelectedItem();
                if (selectedItem == null) {
                    Dialog.showMessageDialog(
                            PosxApp.getApplicationScene(), 
                            "Invalid Action", 
                            "You need to select an item before applying quantity.");
                } else {
                    int decimalsAllowed = selectedItem.getEntity().getInventory().getUseQtyDecimals();
                    if (value.getDecimalLength() > decimalsAllowed) {
                        Dialog.showMessageDialog(
                                PosxApp.getApplicationScene(), 
                                "Invalid Action", 
                                "Decimals allowed in quantity for this item is " + decimalsAllowed);
                    } else {
                        selectedItem.setQuantity(new BigDecimal(value.getValue()).setScale(decimalsAllowed, RoundingMode.HALF_EVEN));
                        focusSearchBox();
                    }
                }
            }
        }, Shortcut.SET_QUANTITY);
        
        setPrimaryAction(1, "Item Discount", true, new NumpadAction() {
            @Override
            public void handle(Numpad.NumpadValue value) {      
                boolean unlimitedDiscount = false;
                // PERM
                if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS__DISCOUNT)) {
                    AuthorizationDialog.show();
                    return;
                }      
                if (sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.DISCOUNT_RECEIPT_PRICE_BELOW_INVENTORY_COST)) {
                    unlimitedDiscount = true;
                } 
                
                ReceiptItemDto selectedItem = receiptManager.getSelectedItem();
                if (selectedItem == null) {
                    Dialog.showMessageDialog(
                            PosxApp.getApplicationScene(),
                            "Invalid Action",
                            "You need to select an item before applying item discount.");
                } else {
                    try {
                        if (value.isPercentage()) {
                            selectedItem.setDiscountPercentage(new BigDecimal(value.getValue()), unlimitedDiscount);
                        } else {
                            selectedItem.setDiscountAmount(new BigDecimal(value.getValue()), unlimitedDiscount);
                        }
                        focusSearchBox();
                    } catch (DtoException ex) {
                        Dialog.showMessageDialog(
                                PosxApp.getApplicationScene(),
                                "Invalid Discount",
                                ex.getMessage());
                    }
                }
            }
        }, Shortcut.SET_ITEM_DISCOUNT);
                      
        setPrimaryAction(2, "Global Discount", true, new NumpadAction() {
            @Override
            public void handle(Numpad.NumpadValue value) {                
                // PERM
                if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.GIVE_GLOBAL_DISCOUNT)) {
                    AuthorizationDialog.show();
                    return;
                }
                
                try {
                    if (value.isPercentage()) {
                        receiptManager.setGlobalDiscountPercentage(new BigDecimal(value.getValue()));
                    } else {
                        receiptManager.setGlobalDiscountAmount(new BigDecimal(value.getValue()));
                    }
                    focusSearchBox();
                } catch (DtoException ex) {
                    Dialog.showMessageDialog(
                            PosxApp.getApplicationScene(),
                            "Invalid Discount",
                            ex.getMessage());
                }
            }
        }, Shortcut.SET_GLOBAL_DISCOUNT);
        
        setExtendedAction(0, "Price", true, new NumpadAction() {
        //setPrimaryAction(2, "Price", true, new NumpadAction() {
            @Override
            public void handle(NumpadValue value) {
                // PERM
                if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS_CHANGE_PRICE)) {
                    AuthorizationDialog.show();
                    return;
                }

                ReceiptItemDto selectedItem = receiptManager.getSelectedItem();
                if (selectedItem == null) {
                    Dialog.showMessageDialog(
                            PosxApp.getApplicationScene(),
                            "Invalid Action",
                            "You need to select an item before applying price.");
                } else {
                    selectedItem.setSellingPrice(new BigDecimal(value.getValue()));
                    focusSearchBox();
                }
            }
        }, Shortcut.SET_SELLING_PRICE);
    }
    
    private void focusSearchBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                receiptViewController.showSearchItemView(null);
            }
        });
    }
}
