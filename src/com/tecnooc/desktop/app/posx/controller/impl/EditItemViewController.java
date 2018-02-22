package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.controller.util.BasicNumpad;
import com.tecnooc.desktop.app.posx.controller.util.Numpad;
import com.tecnooc.desktop.app.posx.controller.util.Shortcut;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutActionListener;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.dto.DtoException;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class EditItemViewController extends AbstractViewController implements InsertableView { 
    @FXML private Label itemNameLabel;
    @FXML private Label quantityLabel;
    @FXML private Label discAmtLabel;
    @FXML private Label discPercLabel;
    @FXML private Label unitPriceLabel;
    @FXML private Label totalLabel;
    @FXML private ToggleGroup itemStatusGroup;
    @FXML private ToggleButton soldButton;
    @FXML private ToggleButton returnedButton;
    @FXML private ToggleButton voidButton;   
    
    @Autowired private ReceiptManager receiptManager;
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private SessionManager sessionManager;
    @Autowired private ReceiptViewController receiptViewController;
    private Numpad numpad;

    @Override
    public void afterPropertiesSet() throws Exception {      
        shortcutManager.setShortcutListener(Shortcut.ITEM_SALE, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (receiptManager.getSelectedItem() != null) {
                    itemStatusGroup.selectToggle(soldButton);
                }
            }
        });   
        shortcutManager.setShortcutListener(Shortcut.ITEM_RETURN, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (receiptManager.getSelectedItem() != null) {
                    itemStatusGroup.selectToggle(returnedButton);
                }
            }
        });   
        shortcutManager.setShortcutListener(Shortcut.ITEM_VOID, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (receiptManager.getSelectedItem() != null) {
                    itemStatusGroup.selectToggle(voidButton);
                }
            }
        });   
        shortcutManager.setShortcutListener(Shortcut.ITEM_DELETE, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (receiptManager.getSelectedItem() != null) {
                    removeItem(null);
                }
            }
        });
        
        itemStatusGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    oldValue.setSelected(true);
                } else {
                    ToggleButton tgl = (ToggleButton) newValue;
                    ReceiptItemDto currentItem = receiptManager.getSelectedItem();
                    switch (tgl.getText().toLowerCase()) {
                        case "sale":
                            currentItem.setTransactionType(ReceiptItemDto.ITEM_SOLD);
                            if (currentItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                                currentItem.setQuantity(BigDecimal.ONE);
                            } else if (currentItem.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                                currentItem.setQuantity(currentItem.getQuantity().negate());
                            }
                            break;
                        case "return":                            
                            // PERM
                            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS_RETURNS)) {
                                itemStatusGroup.selectToggle(soldButton);
                                AuthorizationDialog.show();
                                return;
                            }
                            
                            currentItem.setTransactionType(ReceiptItemDto.ITEM_RETURN);
                            if (currentItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                                currentItem.setQuantity(new BigDecimal(-1));
                            } else if (currentItem.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                                currentItem.setQuantity(currentItem.getQuantity().negate());
                            }
                            break;
                        case "void":                        
                            // PERM
                            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS__VOIDS)) {
                                itemStatusGroup.selectToggle(soldButton);
                                AuthorizationDialog.show();
                                return;
                            }
                            
                            currentItem.setTransactionType(ReceiptItemDto.ITEM_VOID);
                            try {
                                currentItem.setDiscountAmount(BigDecimal.ZERO, true);
                            } catch (DtoException ex) {System.out.println(ex);}
                            currentItem.setQuantity(BigDecimal.ZERO);
                            break;
                    }
                    // receiptViewController.changeStatus(currentItem.getStatus());
                }
            }
        });
        
        receiptManager.selectedItemProperty().addListener(new ChangeListener<ReceiptItemDto>() {
            @Override
            public void changed(ObservableValue<? extends ReceiptItemDto> observable, ReceiptItemDto oldValue, ReceiptItemDto currentItem) {
                if (currentItem == null) {
//                    receiptManager.showSearchItemView(null);
                    return;
                }

                switch (currentItem.getTransactionType()) {
                    case ReceiptItemDto.ITEM_SOLD:
                        soldButton.setSelected(true);
                        break;
                    case ReceiptItemDto.ITEM_RETURN:
                        returnedButton.setSelected(true);
                        break;
                    case ReceiptItemDto.ITEM_VOID:
                        voidButton.setSelected(true);
                        break;
                }

                itemNameLabel.textProperty().bind(currentItem.itemNameProperty());
                quantityLabel.setText(currentItem.quantityProperty().getValue().toString());
                discAmtLabel.setText(currentItem.discountAmountProperty().getValue().toString());
                discPercLabel.setText(currentItem.discountPercentageProperty().getValue().toString());
                unitPriceLabel.setText(currentItem.sellingPriceProperty().getValue().toString());
                totalLabel.setText(currentItem.totalPriceProperty().getValue().toString());
                
                currentItem.quantityProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        quantityLabel.setText(receiptManager.getSelectedItem().getQuantity().toString());
                    }
                });
                
                currentItem.discountAmountProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        discAmtLabel.setText(receiptManager.getSelectedItem().getDiscountAmount().toString());
                    }
                });
                currentItem.discountPercentageProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        discPercLabel.setText(receiptManager.getSelectedItem().getDiscountPercentage().toString());
                    }
                });
                currentItem.sellingPriceProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        unitPriceLabel.setText(receiptManager.getSelectedItem().getSellingPrice().toString());
                    }
                });
                currentItem.totalPriceProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        totalLabel.setText(receiptManager.getSelectedItem().getTotalPrice().toString());
                    }
                });
            }
        });
        
        initNumpad();
    }   
    
    @FXML
    private void removeItem(ActionEvent event) {
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS_DELETE_LINE_ITEM)) {
            AuthorizationDialog.show();
            return;
        }
        
        Dialog.showMessageDialog(
                getView().getScene(), 
                "Delete Item", 
                "This action will delete '" + itemNameLabel.getText() + "'. Are you sure?",
                Dialog.DialogType.OK_CANCEL,
                new DialogListener() {
            @Override
            public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                if (buttonPressed == Dialog.ButtonType.OK) {
                    receiptManager.removeSelectedItem();
                }
            }
        });
    }

    private void initNumpad() {
        numpad = new BasicNumpad(getView(), receiptViewController, receiptManager, shortcutManager, sessionManager);
        AnchorPane view = (AnchorPane) getView();

        view.getChildren().add(numpad);

        numpad.setPrefWidth(352.0);
        numpad.setPrefHeight(192.0);
        AnchorPane.setLeftAnchor(numpad, 0.0);
        AnchorPane.setRightAnchor(numpad, 0.0);
        AnchorPane.setTopAnchor(numpad, 240.0);
    }

    @Override
    public void beforeInsert() {}
    @Override
    public void afterInsert() {}
}
