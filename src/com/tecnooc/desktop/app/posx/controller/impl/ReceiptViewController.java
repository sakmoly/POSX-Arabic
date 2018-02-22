package com.tecnooc.desktop.app.posx.controller.impl;

import com.lowagie.text.DocumentException;
import com.tecnooc.desktop.app.posx.PosxApp;
import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.InsertableView;
import com.tecnooc.desktop.app.posx.controller.util.PTableColumn;
import com.tecnooc.desktop.app.posx.controller.util.ReceiptItemRow;
import com.tecnooc.desktop.app.posx.controller.util.ReceiptRow;
import com.tecnooc.desktop.app.posx.controller.util.ReceiptTenderRow;
import com.tecnooc.desktop.app.posx.controller.util.ReceiptViewListManager;
import com.tecnooc.desktop.app.posx.controller.util.Shortcut;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutActionListener;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import com.tecnooc.desktop.app.posx.service.ApplicationPreferenceValueService;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author jomit
 */
@Component
public class ReceiptViewController extends AbstractViewController {
    @Autowired private SearchCustomerViewController searchCustomerViewController;
    @Autowired private SearchItemViewController     searchItemViewController;
    @Autowired private EditItemViewController       editItemViewController;
    @Autowired private TenderViewController         tenderViewController;
    @Autowired private EditTenderViewController     editTenderViewController;
    @Autowired private ReceiptListViewController    receiptListViewController;
    
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private ReceiptManager  receiptManager;
    @Autowired private SessionManager  sessionManager;
    private ReceiptViewListManager     receiptViewListManager;
    
    @FXML private StackPane detailsPane;
    @FXML private Button customerButton;
    @FXML private Label totalLabel;
    @FXML private Button saveReceiptButton;
    //@FXML private Button exportReceiptButton;
    @FXML private Button deleteReceiptButton;
    @FXML private Button printReceiptButton;
    @FXML private Label billNumberLabel;
    @FXML private Button holdButton;
    
    @FXML private TableView<ReceiptRow> receiptTable;
    @FXML private PTableColumn<ReceiptRow, String> serialNoColumn;
    @FXML private PTableColumn<ReceiptRow, String> descriptionColumn;
    @FXML private PTableColumn<ReceiptRow, Integer> quantityColumn;
    @FXML private PTableColumn<ReceiptRow, BigDecimal> discountColumn;
    @FXML private PTableColumn<ReceiptRow, BigDecimal> priceColumn;
    @FXML private PTableColumn<ReceiptRow, BigDecimal> taxAmountColumn;
    @FXML private PTableColumn<ReceiptRow, BigDecimal> totalColumn;
    
    private boolean programaticalSelection = false;
    private int lastSelectionIndex = 0;

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         * The Hack
         * 
         * Dismissing the "Item Not Found" dialog when searching for an item 
         * with <Enter> key works as expected. The focus gets back to lookup textfield.
         * But doing the same with mouse, ie., pressing the OK button of the dialog
         * will bring focus to customerButton. Currently, I have no idea why this happens,
         * thus this hack.
         */
        customerButton.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
                if (focused) {
                    searchItemViewController.focus();
                }
            }
        });
        
        customerButton.textProperty().bind(receiptManager.getCurrentCustomer().fullNameProperty());
        receiptManager.currentCustomerProperty().addListener(new ChangeListener<CustomerDto>() {
            @Override
            public void changed(ObservableValue<? extends CustomerDto> observable, CustomerDto oldValue, CustomerDto newValue) {
                if (newValue == null) {
                    customerButton.textProperty().unbind();
                    customerButton.setText("");
                } else {
                    customerButton.textProperty().bind(newValue.fullNameProperty());
                }
            }
        });
        
        receiptViewListManager = new ReceiptViewListManager(receiptManager);
        receiptTable.setItems(receiptViewListManager.receiptListProperty());

        serialNoColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, String>("serialNo"));
        descriptionColumn.setCellFactory(new Callback<TableColumn<ReceiptRow, String>, TableCell<ReceiptRow, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                TableCell cell = new TableCell<ReceiptRow, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                        setGraphic(null);
                    }

                    private String getString() {
                        return getItem() == null ? "" : getItem().toString();
                    }
                };

                cell.setStyle("-fx-alignment: CENTER-LEFT;");
                return cell;
            }
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, String>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, Integer>("quantity"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, BigDecimal>("discount"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, BigDecimal>("price"));
        taxAmountColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, BigDecimal>("taxAmount"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<ReceiptRow, BigDecimal>("total"));
        
        receiptTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ReceiptRow>() {
            @Override
            public void changed(ObservableValue<? extends ReceiptRow> observable, ReceiptRow oldValue, ReceiptRow newValue) {
                final int currentSelectionIndex = receiptTable.getSelectionModel().getSelectedIndex();
                
                if (newValue != null && newValue instanceof ReceiptItemRow) {
                    ReceiptItemRow row = (ReceiptItemRow) newValue;
                    receiptManager.selectItem(row.getReceiptItem());
                    if (!programaticalSelection) {
                        showEditItemView();
                    }
                } else if (newValue != null && newValue instanceof ReceiptTenderRow) {
                    ReceiptTenderRow row = (ReceiptTenderRow) newValue;
                    receiptManager.selectTender(row.getTender());
                    showEditTenderView();
                } else {
                    int size = receiptTable.getItems().size();
                    if (receiptTable.getSelectionModel().getSelectedIndex() == (size - 1)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                receiptTable.getSelectionModel().selectFirst();
                            }
                        });
                    } else {
                        if (lastSelectionIndex < currentSelectionIndex) {                            
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    receiptTable.getSelectionModel().selectNext();
                                }
                            });
                        } else {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    receiptTable.getSelectionModel().select(currentSelectionIndex - 1);
                                }
                            });
                        }
                    }
                    
                }
                //programaticalSelection = false;
                lastSelectionIndex = currentSelectionIndex;
            }
        });
        
        receiptTable.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ReceiptRow selectedItem = receiptTable.getSelectionModel().getSelectedItem();
                
                if (selectedItem != null && selectedItem instanceof ReceiptItemRow) {
                    ReceiptItemRow row = (ReceiptItemRow) selectedItem;
                    receiptManager.selectItem(row.getReceiptItem());
                    showEditItemView();
                } else if (selectedItem != null && selectedItem instanceof ReceiptTenderRow) {
                    ReceiptTenderRow row = (ReceiptTenderRow) selectedItem;
                    receiptManager.selectTender(row.getTender());
                    showEditTenderView();
                } else {
                    //showSearchItemView(null);
                    programaticalSelection = true;
                }
            }
        });
        
        receiptManager.totalProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue, BigDecimal newValue) {
                boolean visibility = false;
                if (newValue == null || receiptManager.isCurrentReceiptEmpty()) {
                    totalLabel.setText("");
                } else if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                    totalLabel.setText("Due " + newValue.setScale(2, RoundingMode.HALF_EVEN));
                } else {
                    totalLabel.setText("Change " + newValue.setScale(2, RoundingMode.HALF_EVEN).abs());
                    
                    if (newValue.compareTo(BigDecimal.ZERO) <= 0) {
                        if (receiptManager.isTendered()) {
                            if (receiptManager.hasReturnedItem()) {
                                /*BigDecimal saledSum = BigDecimal.ZERO;
                                BigDecimal returnedSum = BigDecimal.ZERO;
                                for (ReceiptItemDto receiptItemDto : receiptManager.currentItemsProperty()) {
                                    if (receiptItemDto.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
                                        saledSum = saledSum.add(receiptItemDto.getTotalPrice());
                                    } else {
                                        returnedSum = returnedSum.add(receiptItemDto.getTotalPrice());
                                    }
                                }
                                
                                if (saledSum.compareTo(returnedSum.abs()) >= 0) {
                                    visibility = true;
                                }*/
                                BigDecimal change = receiptManager.subtotalProperty().get().subtract(receiptManager.totalProperty().get());
                                if (change.compareTo(BigDecimal.ZERO) == 0) {
                                    change = receiptManager.subtotalProperty().get();
                                }
                                totalLabel.setText("Change " + change.setScale(2, RoundingMode.HALF_EVEN).abs());
                                visibility = true;
                            } else {
                                visibility = true;
                            }
                        } 
                    } else {                        
                        visibility = true;
                    }
                }
                
                saveReceiptButton.setVisible(visibility);
                //exportReceiptButton.setVisible(visibility);
                printReceiptButton.setVisible(visibility);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        programaticalSelection = true;
                        receiptTable.getSelectionModel().select(receiptManager.currentItemsProperty().size() - 1);
                    }
                });
            }
        });
        
        receiptTable.setRowFactory(new Callback<TableView<ReceiptRow>, TableRow<ReceiptRow>>() {
            @Override
            public TableRow<ReceiptRow> call(TableView<ReceiptRow> p) {
                final TableRow<ReceiptRow> row = new TableRow<ReceiptRow>() {
                    @Override
                    public void updateItem(final ReceiptRow receiptRow,
                            final boolean emty) {
                        super.updateItem(receiptRow, emty);
                        if (!(receiptRow instanceof ReceiptItemRow)) {
                            getStyleClass().add("table-row-bold");
                        }
                    }
                };
                return row;
            }
        });
        billNumberLabel.textProperty().bind(receiptManager.invoiceNumberProperty().asString());
        
        holdButton.setText("HOLD(" + receiptManager.heldReceiptsProperty().size() + ")");
        receiptManager.heldReceiptsProperty().addListener(new ListChangeListener<ReceiptDto>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReceiptDto> c) {
                holdButton.setText("HOLD(" + receiptManager.heldReceiptsProperty().size() + ")");
            }
        });
        
        // TODO: Solve Bug ---   slowness
        receiptViewListManager.receiptListProperty().addListener(new ListChangeListener<ReceiptRow>() {
            private boolean scrolledTo = false;
            
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReceiptRow> c) {
                programaticalSelection = true;
                if (scrolledTo) {
                    return;
                }
                
                if (receiptViewListManager.receiptListProperty().size() >= 10) {
                    Set<Node> scrollBars = receiptTable.lookupAll(".scroll-bar");
                    for (Node node : scrollBars) {
                        if (node instanceof ScrollBar && node.isVisible()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {                                    
                                    receiptTable.scrollTo(receiptViewListManager.receiptListProperty().size() - 2);
                                    scrolledTo = true;
                                    System.out.println("scrolled....");
                                }
                            });
                        }
                    }
                    //receiptTable.scrollTo(receiptViewListManager.receiptListProperty().size() - 2);
                }
            }
        });
        
        showSearchItemView(null);
        
        // Shortcuts Management
        shortcutManager.setShortcutListener(Shortcut.SEARCH_CUSTOMER, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                searchCustomer(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.NEW_RECEIPT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                startNewReceipt(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.DELETE_RECEIPT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                removeCurrentReceipt(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.VIEW_SEARCH_ITEM_BOX, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                showSearchItemView(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.VIEW_TENDER_BOX, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                showTenderView(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.VIEW_HOLD_BOX, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                showHoldReceiptsView(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.SAVE_RECEIPT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (saveReceiptButton.isVisible()) {
                    saveCurrentReceipt(null);
                }
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.PRINT_RECEIPT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (printReceiptButton.isVisible()) {
                    printCurrentReceipt(null);
                }
            }
        });
        
        
        shortcutManager.setShortcutListener(Shortcut.SELECT_FIRST_ITEM, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                programaticalSelection = false;
                int numItems = receiptManager.currentItemsProperty().size();
                if (numItems > 0) {
                    receiptTable.getSelectionModel().selectFirst();
                    if (receiptViewListManager.receiptListProperty().size() > 4) {
                        receiptTable.scrollTo(0);
                    }
                    receiptTable.requestFocus();
                }
            }
        });        
        shortcutManager.setShortcutListener(Shortcut.SELECT_LAST_ITEM, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                programaticalSelection = false;
                int numItems = receiptManager.currentItemsProperty().size();
                if (numItems > 0) {
                    receiptTable.getSelectionModel().select(numItems - 1);
                    if (receiptViewListManager.receiptListProperty().size() > 4) {
                        receiptTable.scrollTo(receiptViewListManager.receiptListProperty().size() - 2);
                    }
                    receiptTable.requestFocus();
                }
            }
        });        
        shortcutManager.setShortcutListener(Shortcut.SELECT_FIRST_TENDER, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                int numTenders = receiptManager.currentTendersProperty().size();
                int numItems = receiptManager.currentItemsProperty().size();
                int numEntries = receiptViewListManager.receiptListProperty().size();
                
                if (numTenders > 0) {
                    int difference = numEntries - (numItems + numTenders);
                    int tenderindex = numTenders + 1;
                    if (difference == 3) {
                        tenderindex = numTenders + 2;
                    }
                    
                    receiptTable.getSelectionModel().select(tenderindex);
                }
                
                if (receiptViewListManager.receiptListProperty().size() > 4) {
                    receiptTable.scrollTo(receiptViewListManager.receiptListProperty().size() - 2);
                }
                receiptTable.requestFocus();                
            }
        });        
        shortcutManager.setShortcutListener(Shortcut.SELECT_LAST_TENDER, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                int numTenders = receiptManager.currentTendersProperty().size();
                int numEntries = receiptViewListManager.receiptListProperty().size();
                
                if (numTenders > 0) {                    
                    receiptTable.getSelectionModel().select(numEntries - 2);
                }

                if (receiptViewListManager.receiptListProperty().size() > 4) {
                    receiptTable.scrollTo(receiptViewListManager.receiptListProperty().size() - 2);
                }
                receiptTable.requestFocus();
            }
        });    
    }
    
    
    
    @FXML
    private void searchCustomer(ActionEvent ae) {          
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ACCESS_CUSTOMERS)) {
            AuthorizationDialog.show();
            return;
        }    
        
        searchCustomerViewController.show(getView().getScene(), new DialogListener() {
            @Override
            public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                if (buttonPressed == Dialog.ButtonType.OK) {                    
                    receiptManager.setCurrentCustomer(searchCustomerViewController.getSelectedCustomer());
                }
            }
        });
    }
    
    @FXML
    private void startNewReceipt(ActionEvent ae) {          
        if (receiptManager.isCurrentReceiptEmpty()) {
            searchItemViewController.focus();
            return;
        }
         
        if (receiptManager.currentTendersProperty().size() > 0) {
            Dialog.showMessageDialog(getView().getScene(), "Error", 
                    "The receipt has been tendered. It cannot be placed on hold.");
        } else {    
            // PERM
            if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.DISCARD_RECEIPT_IN_PROGRESS)) {
                AuthorizationDialog.show();
                return;
            }
            
            Dialog.showMessageDialog(
                    getView().getScene(),
                    "Open New Receipt",
                    "This action will open a new receipt and current one will be held. Are you sure?",
                    Dialog.DialogType.OK_CANCEL,
                    new DialogListener() {
                @Override
                public void dialogDismissed(Dialog.ButtonType pressedButton) {
                    if (pressedButton == Dialog.ButtonType.OK) {
                        receiptManager.openNewReceipt();
                        showSearchItemView(null);
                    }
                }
            });
        }
    }
    
    @FXML
    private void removeCurrentReceipt(ActionEvent ae) {         
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.DISCARD_RECEIPT_IN_PROGRESS)) {
            AuthorizationDialog.show();
            return;
        }    
        
        if (receiptManager.isCurrentReceiptEmpty()) {            
            searchItemViewController.focus();
            return;
        }
        
        if (receiptManager.isTendered()) {
            Dialog.showMessageDialog(getView().getScene(), "Delete Receipt", "The receipt has been tendered. It cannot be deleted.");
            return;
        }
        
        Dialog.showMessageDialog(
                getView().getScene(),
                "Delete Receipt",
                "This action will delete the current receipt. Are you sure?",
                Dialog.DialogType.OK_CANCEL,
                new DialogListener() {
            @Override
            public void dialogDismissed(Dialog.ButtonType pressedButton) {
                if (pressedButton == Dialog.ButtonType.OK) {
                    receiptManager.removeReceipt();
                    showSearchItemView(null);
                }
            }
        });
    }
    
    @FXML
    private void saveCurrentReceipt(ActionEvent ae) { 
//        BigDecimal total = receiptManager.totalProperty().get();
//        if (receiptManager.hasReturnedItem() && receiptManager.totalProperty().get().compareTo(BigDecimal.ZERO) < 0) {
//            BigDecimal saledSum = BigDecimal.ZERO;
//            BigDecimal returnedSum = BigDecimal.ZERO;
//            for (ReceiptItemDto receiptItemDto : receiptManager.currentItemsProperty()) {
//                if (receiptItemDto.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
//                    saledSum = saledSum.add(receiptItemDto.getTotalPrice());
//                } else {
//                    returnedSum = returnedSum.add(receiptItemDto.getTotalPrice());
//                }
//            }
//
//            if (saledSum.compareTo(returnedSum.abs()) >= 0) {
//                visibility = true;
//            }
//            
//            Dialog.showMessageDialog(getView().getScene(), "Error", "Change must be zero for receipts with returned items.");
//            return;
//        }
        
        receiptManager.updateReceipt();
        showSearchItemView(null);
    }
    
    @FXML
    private void printCurrentReceipt(ActionEvent ae) { 
//        if (receiptManager.hasReturnedItem() && receiptManager.totalProperty().get().compareTo(BigDecimal.ZERO) < 0) {
//            Dialog.showMessageDialog(getView().getScene(), "Error", "Change must be zero for receipts with returned items.");
//            return;
//        }
        
        printReceipt();
        saveCurrentReceipt(null);
        
//        Dialog.showMessageDialog(
//                getView().getScene(),
//                "Choose Action",
//                "What you want to do?\nPrint the receipt or export it as PDF?",
//                Dialog.DialogType.PRINT_EXPORT_CANCEL,
//                new DialogListener() {
//            @Override
//            public void dialogDismissed(Dialog.ButtonType pressedButton) {
//                if (pressedButton == Dialog.ButtonType.PRINT) {
//                    printReceipt();
//                    saveCurrentReceipt(null);
//                } else if (pressedButton == Dialog.ButtonType.EXPORT) {
//                    exportReceipt();
//                    saveCurrentReceipt(null);
//                }
//            }
//        });
    }
    
    //@FXML
    private void exportCurrentReceipt(ActionEvent ae) {        
        exportReceipt();
        saveCurrentReceipt(null);
    }
    
    @FXML
    public void showSearchItemView(ActionEvent ae) {  
        showView(searchItemViewController);
    }
    
    @FXML
    public void showTenderView(ActionEvent ae) { 
        if (receiptManager.currentItemsProperty().isEmpty()) {
            return;
        }
        
        showView(tenderViewController);
    }
    
    @FXML
    private void showHoldReceiptsView(ActionEvent ae) {          
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.HOLD_UNHOLD_RECEIPT)) {
            AuthorizationDialog.show();
            return;
        }    
        
        showView(receiptListViewController);
    }
    
    private void showEditItemView() {
        showView(editItemViewController);
    }
    
    private void showEditTenderView() {
        showView(editTenderViewController);
    }

    private void showView(AbstractViewController viewController) {
        ObservableList<Node> viewList = detailsPane.getChildren();
        Node viewToShow = viewController.getView();
        InsertableView current = (InsertableView) viewController;

        current.beforeInsert();
        if (viewList.isEmpty()) {
            viewList.add(viewToShow);
        } else if (viewList.get(0) != viewToShow) {
            viewList.remove(0);
            viewList.add(viewToShow);
        }
        current.afterInsert();
    }
    
    /**
     *              Store Name
     *              Address 1
     *               Phone 1
     * ‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒
     * Receipt#: 1          WS#:     1      
     * 15/06/2013 6:57:34PM Cashier: Tom
     * Bill To:  Abdullah                   
     * 
     * 6281100870074     2 x    100.00      
     *   RAID COKRCH ANT KILR RED     200.00
     * _____________  ____ x ______.__
     *   _________________________ ______.__
     * _____________  ____ x ______.__
     *   _________________________ ______.__
     *                       Total    900.00
     * Number of items: 3
     *                        Cash    450.00
     *                Debit/Credit    450.00
     *                      Change      0.00
     * ‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒
     *        Thank you for shipping
     * 
     */
    private String getPrintableReceipt() {
        //final String HR = "‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒";
        final String HR = "-------------------------------------";
        StringBuilder builder = new StringBuilder(360);
        String address2 = sessionManager.getStore().getEntity().getAddress2();
        String address3 = sessionManager.getStore().getEntity().getAddress3();
        String address4 = sessionManager.getStore().getEntity().getAddress4();
        String address5 = sessionManager.getStore().getEntity().getAddress5();
        
        String address  = "";
        address += address2.isEmpty() ? "" : centerText(address2) + "\n";
        address += address3.isEmpty() ? "" : centerText(address3) + "\n";
        address += address4.isEmpty() ? "" : centerText(address4) + "\n";
        address += address5.isEmpty() ? "" : centerText(address5) + "\n";
        
        builder.append(centerText(sessionManager.getStore().getEntity().getStoreName())).append('\n');
        builder.append(centerText(sessionManager.getStore().getEntity().getAddress1())).append('\n');
        builder.append(centerText(address)).append('\n');
        builder.append(centerText(sessionManager.getStore().getEntity().getPhone1())).append('\n');
        builder.append(HR).append('\n');
        
        String format = "Receipt#: %-12d WS#: %d";
        String line   = String.format(format, receiptManager.invoiceNumberProperty().get(), 
                                              sessionManager.getTerminal().getEntity().getTerminalLogref());
        builder.append(line).append('\n');
        
        format = "%s";
        line   = String.format(format, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
        builder.append(line).append('\n');
        
        format = "Served By*: %s";
        line   = String.format(format, sessionManager.getUser().getEntity().getUserName());
        builder.append(line).append('\n');
        
        if (!receiptManager.getCurrentCustomer().isDefaultCustomer()) {
            format = "Bill To:  %-27s";
            line = String.format(format, receiptManager.getCurrentCustomer().fullNameProperty().get());
            builder.append(line).append('\n');
        }
        
        builder.append('\n');
        
        // "%-13s %5.2fx%7.2f %8.2f\n"
        builder.append("Name          Qty.  Price   Total\n");
        builder.append(line).append('\n');
        builder.append(line).append('\n');
        builder.append("Description                      \n");
        
        int itemCount = 0;
        boolean itemCountPrinted = false;
        BigDecimal totalWithoutTax = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal netTotal = BigDecimal.ZERO;
        for (ReceiptRow receiptRow : receiptViewListManager.receiptListProperty()) {
            if (receiptRow instanceof ReceiptItemRow) {
                itemCount++;
                
                ReceiptItemRow row = (ReceiptItemRow) receiptRow;
                ReceiptItemDto item = row.getReceiptItem();
                BigDecimal total  = item.getTotalPrice().subtract(item.taxAmountProperty().get());
                
                totalWithoutTax = totalWithoutTax.add(total);
                totalTax = totalTax.add(item.taxAmountProperty().get());
                netTotal = netTotal.add(item.getTotalPrice());
//                if (!sessionManager.getPreferedLanguage().equals(ApplicationPreferenceValueService.LANG_ARABIC)) {
                    format = "%-13s %5.2fx%7.2f %8.2f\n  %s";
                    line = String.format(format, item.getEntity().getInventory().getUpc(),
                            item.getQuantity().doubleValue(),
                            item.getSellingPrice().subtract(item.getDiscountAmount()).doubleValue(),
                            total.doubleValue(),
                            trimText(item.getDescription2(), 35));
//                } else {
//                    format = "%-13s  %5.2f x %8.2f\n  %-24s %8.2f";
//                    line = String.format(format, item.getEntity().getInventory().getUpc(),
//                            item.getQuantity().doubleValue(),
//                            item.getSellingPrice().subtract(item.getDiscountAmount()).doubleValue(),
//                            trimText(item.getItemName(), 25),
//                            total.doubleValue());
//                }
                builder.append(line).append('\n');
            } else {
                if (!itemCountPrinted) {
                    itemCountPrinted = true;
                    builder.append("\n");
                    builder.append("Number of items: ").append(itemCount).append('\n');
                }
                
                String description = receiptRow.descriptionProperty().get();
                format             = "%27s %8.2f";
                
                if (description.equals("Balance")) {
                    description = "Change";
                    line = String.format(format, description, receiptRow.totalProperty().get().doubleValue());
                    builder.append(line).append('\n');
                } else if (description.equals("Total")) {
                    line = String.format(format, description, totalWithoutTax.doubleValue());
                    builder.append(line).append('\n');
                } else if (description.equals("Tax")) {
                    description = "VAT 5%";
                    line = String.format(format, description, totalTax.doubleValue());
                    builder.append(line).append('\n');
                    
                    description = "Net Total";
                    line = String.format(format, description, netTotal.doubleValue());
                    builder.append(line).append('\n');
                } else {
                    line = String.format(format, description, receiptRow.totalProperty().get().doubleValue());
                    builder.append(line).append('\n');
                }
            }
        }
        
        builder.append(HR).append('\n');
        builder.append(centerText("Thankyou for shopping")).append('\n');
        
        return builder.toString();
    }
    
    private String centerText(String str) {
        if (str == null) {
            return "";
        }
        
        str = trimText(str, 36);
        int spaces = (36 - str.length()) / 2;
        StringBuilder builder = new StringBuilder(36);
        
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
        builder.append(str);
        
        return builder.toString();
    }
    
    private String trimText(String str, int length) {
        if (str == null) {
            return "";
        }
        
        if (str.length() > length) {
            str = str.substring(0, length - 3) + "...";
        }
        
        return str;
    }
    
    
    public void printReceipt() {
        File jarFile = new File(ReceiptViewController.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarDir = jarFile.getParentFile().getPath();
        String logo = jarDir + File.separator + "logo.png";
        if (!logo.startsWith("/")) {
            logo = "/" + logo; // for getting file:/// on windows.
        }
        String out  = getPrintableReceipt();
        String html = "<html><head><style>html,body{margin:0;padding:0;}img{width:150pt;}pre{font-family: monospaced;font-size: 8pt;font-weight: bold;}th{text-align: center;}</style></head><body>"
                + ""
                + "<table>"
                + "<tr><th><img src=\"file://" + logo + "\"></th></tr>"
                + "<tr><td><pre>" + out + "</pre></td></tr>"
                + "</table>"
                + "</body></html>";
        
        System.out.println(html);
        //JTextComponent c = new JTextArea(out);
        JEditorPane c = new JEditorPane("text/html", html);
        c.setFont(new Font(Font.MONOSPACED, Font.BOLD, 8));
        try {
            PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();
            attrSet.add(MediaSizeName.ISO_A7);
            attrSet.add(OrientationRequested.PORTRAIT);
            attrSet.add(new MediaPrintableArea(3, 3, 66, 90, MediaPrintableArea.MM));
            
            c.print(null, null, false, null, attrSet, false);
        } catch (PrinterException ex) {
            Dialog.showMessageDialog(PosxApp.getApplicationScene(), "Error in Printing", ex.getMessage());
        }
    }
    
    public void exportReceipt() {
        Date now = new Date();
        String out = "<!DOCTYPE html>"
                + "<html>"
                + "    <head>"
                + "        <title>INVOICE</title>"
                + "        <style type=\"text/css\">" 
                + "            body {font-size: 8pt;}"
                + "            @page {"
                + "                size: 74mm 105mm;"
                + "                background: white;"
                + "                margin: 5mm;"
                + "            }"
                + "        </style>"
                + "    </head>"
                + "    <body>"
                + "            <div>"
                + "                <pre>"
                + getPrintableReceipt()
                + "                </pre>"
             + "            </div>"
             + "    </body>"
             + "</html>";

        System.out.println(out);
        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(getView().getScene().getWindow());
        if (file != null) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(out);
            renderer.layout();
            try {
                OutputStream os = new FileOutputStream(file);
                renderer.createPDF(os);
                os.close();
            } catch (DocumentException | IOException ex) {
                Dialog.showMessageDialog(getView().getScene(), "Error Exporting", ex.getMessage());
            }
        }
    }
}
