package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.controller.util.BasicNumpad;
import com.tecnooc.desktop.app.posx.controller.util.InventoryItemHBox;
import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.controller.util.Numpad;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import com.tecnooc.desktop.app.posx.service.InventoryNumberService;
import com.tecnooc.desktop.app.posx.service.InventoryService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author jomit
 */
public class SearchItemViewController extends AbstractViewController implements ApplicationContextAware, InsertableView {  
    private ApplicationContext applicationContext;
    
    @FXML private TextField lookupTextField;
    @FXML private ListView<InventoryItemDto> itemList;
    
    @Autowired private InventoryService service;
    @Autowired private InventoryNumberService inventoryNumberService;
    @Autowired private ReceiptManager receiptManager;
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private SessionManager sessionManager;
    @Autowired private ReceiptViewController receiptViewController;
    
    private ObservableList<InventoryItemDto> items;
    private boolean keytypeSuggest;
    private Numpad numpad;
    
    private boolean isBarcodeItem;
    private boolean hasBarcodePrice;
    private double barcodeItemQuantity;
    private double barcodeItemPrice;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        items = FXCollections.observableArrayList();
        itemList.setItems(items);
        
        itemList.setCellFactory(new Callback<ListView<InventoryItemDto>, ListCell<InventoryItemDto>>() {
            @Override
            public ListCell<InventoryItemDto> call(ListView<InventoryItemDto> param) {
                ListCell<InventoryItemDto> cell = new ListCell<InventoryItemDto>() {
                    @Override
                    protected void updateItem(InventoryItemDto item, boolean empty) {
                        super.updateItem(item, empty);
                        InventoryItemHBox holder = applicationContext.getBean(InventoryItemHBox.class);
                        if (empty) {
                            holder.setName(null);
                            holder.setPrice(null);
                        } else {
                            holder.setName(item.getItemName());
                            holder.setPrice(item.getPrice());                            
                        }
                        setGraphic(holder);
                    }
                };                
                
                return cell;
            }
        });
        
        itemList.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                //System.out.println("KeyEvent: " + event);
                if (event.getCode() == KeyCode.ENTER) {
                    //System.out.println("Adding Item.");
                    addItem(null);
                }
            }
        });
        
        initNumpad();
        
        // Kinda hack to get it focused when logged in.
        lookupTextField.needsLayoutProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean needsLayout) {
                if (!needsLayout) {
                    focus();
                }
            }
        });
    }
    
    private void lookupItem() {                        
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS_ADD)) {AuthorizationDialog.show();
            AuthorizationDialog.show();
            return;
        }
                
        if (keytypeSuggest) {
            items.clear();
            if (lookupTextField.getText().isEmpty()) {
                keytypeSuggest = false;
            } else {
                try {
                    String lookupCode = lookupTextField.getText();
                    // check whether the entered or scanned value is weighing item barcode.
                    String weighingItemPrefix = sessionManager.getWeighingItemPrefix();
                    String nonWeighingItemPrefix = sessionManager.getNonWeighingItemPrefix();
                    isBarcodeItem = false;
                    
                    int prefixLength = 0;
                    int codeLength = 0;
                    int quantityLength = 0;
                    int priceLength = 0;
                    
                    if (!weighingItemPrefix.equals("") && lookupCode.startsWith(weighingItemPrefix)) {
                        // weighing item, parse it
                        prefixLength = weighingItemPrefix.length();
                        codeLength = sessionManager.getWeighingItemCodeLength();
                        quantityLength = sessionManager.getWeighingItemQuantityLength();
                        priceLength = sessionManager.getWeighingItemPriceLength();
                    } else if (!nonWeighingItemPrefix.equals("") && lookupCode.startsWith(nonWeighingItemPrefix)) {
                        // non-weighing item, parse it
                        prefixLength = nonWeighingItemPrefix.length();
                        codeLength = sessionManager.getNonWeighingItemCodeLength();
                        quantityLength = 0;
                        priceLength = sessionManager.getNonWeighingItemPriceLength();
                    }
                    
                    if (prefixLength > 0) {
                        int totalLength = prefixLength + codeLength + quantityLength + priceLength;
                        
                        if (lookupCode.length() < totalLength) {
                            items.addAll(service.lookup(lookupCode));
                        } else {
                            String code = lookupCode.substring(prefixLength, prefixLength + codeLength);
                            // check code exists
                            InventoryItemDto item = service.lookupByCode(code);
                            if (item == null) {
                                items.addAll(service.lookup(lookupCode));
                            } else {
                                items.add(item);
                                isBarcodeItem = true;
                                hasBarcodePrice = priceLength > 0;

                                int quantityStart = prefixLength + codeLength;
                                int quantityEnd = quantityStart + quantityLength;
                                if (quantityLength > 0) {
                                    String quantityString = lookupCode.substring(quantityStart, quantityEnd);
                                    if (quantityLength <= 3) {
                                        quantityString = "." + quantityString;
                                    } else {
                                        // last 3 digits decimals
                                        quantityString = quantityString.substring(0, quantityLength - 3) +
                                                "." + quantityString.substring(quantityLength - 3);
                                    }
                                    quantityString = quantityString.replaceFirst("^0+(?!$)", "");
                                    barcodeItemQuantity = Double.parseDouble(quantityString);
                                } else {
                                    barcodeItemQuantity = 1;                                    
                                }

                                if (hasBarcodePrice) {
                                    String priceString = lookupCode.substring(quantityEnd);
                                    if (priceLength <= 2) {
                                        priceString = "." + priceString;
                                    } else {
                                        // last 2 digits decimals
                                        priceString = priceString.substring(0, priceLength - 2)
                                                + "." + priceString.substring(priceLength - 2);
                                    }
                                    priceString = priceString.replaceFirst("^0+(?!$)", "");
                                    barcodeItemPrice = Double.parseDouble(priceString);
                                }                       
                            }
                        }
                    } else {
                        List<InventoryItemDto> entries = inventoryNumberService.lookupInventoryItem(lookupCode);
                        if (entries.isEmpty()) {
                            entries = service.lookup(lookupCode);
                        }
                        items.addAll(entries);
                    }
                } catch (Exception ex) {
                    Util.showInternalExceptionMessage(ex);
                }
                
                if (items.isEmpty()) {          
                    Dialog.showMessageDialog(
                            getView().getScene(), 
                            "Item Not Found", 
                            "\"" + lookupTextField.getText() + "\" could not be found.");
                    lookupTextField.setText("");
                    //focus();
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
        
        lookupItem();
        
        if (ch == '\n' || ch == '\r') {
            if (items.size() == 1) {
                InventoryItemDto item = items.get(0);
                if (isBarcodeItem) {
                    isBarcodeItem = false;
                    if (hasBarcodePrice) {
                        receiptManager.addItem(lookupTextField.getText(), item, new BigDecimal(barcodeItemQuantity).setScale(3, RoundingMode.HALF_EVEN), barcodeItemPrice);
                    } else {
                        receiptManager.addItem(lookupTextField.getText(), item, new BigDecimal(barcodeItemQuantity).setScale(3, RoundingMode.HALF_EVEN));
                    }
                } else {
                    receiptManager.addItem(lookupTextField.getText(), item);
                }
                lookupTextField.setText("");
                keytypeSuggest = false;
                items.clear();
                focus();
            } else if (items.size() > 0) {
                //itemList.selectionModelProperty().get().selectFirst();
                itemList.requestFocus();
            }
        }
    }    
    
    @FXML
    public void addItem(MouseEvent event) {   
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ITEMS_ADD)) {
            AuthorizationDialog.show();
            return;
        }
        
        InventoryItemDto item = (InventoryItemDto) itemList.getSelectionModel().getSelectedItem();
        if (item  != null) {
            receiptManager.addItem(lookupTextField.getText(), item);
            lookupTextField.setText("");
            keytypeSuggest = false;
            items.clear();
            focus();
        } 
    }
    
    private void initNumpad() {
        numpad = new BasicNumpad(getView(), receiptViewController, receiptManager, shortcutManager, sessionManager);
        AnchorPane view = (AnchorPane) getView();

        view.getChildren().add(numpad);

        numpad.setPrefWidth(352.0);
        numpad.setPrefHeight(192.0);
        AnchorPane.setLeftAnchor(numpad, 0.0);
        AnchorPane.setRightAnchor(numpad, 0.0);
        AnchorPane.setBottomAnchor(numpad, 0.0);
    }
    
    public void focus() {
        lookupTextField.requestFocus();
    }

    @Override
    public void beforeInsert() {
    }
    @Override
    public void afterInsert() {
        keytypeSuggest = false;
        items.clear();
        lookupTextField.setText("");
        focus();
    }
}
