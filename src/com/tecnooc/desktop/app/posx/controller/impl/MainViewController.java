package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.util.Shortcut;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutActionListener;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.desktop.app.posx.dto.UserPermissionsDto;
import com.tecnooc.desktop.app.posx.dto.ZoutDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import com.tecnooc.desktop.app.posx.manager.ReportManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import com.tecnooc.desktop.app.posx.service.ZoutService;
import java.math.BigDecimal;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class MainViewController extends AbstractViewController {    
    private Stage primaryStage;
    @Autowired private LoginViewController loginViewController;
    @Autowired private ReceiptViewController receiptViewController;
    @Autowired private OpenDialogViewController openDialogViewController;
    @Autowired private CloseDialogViewController closeDialogViewController;
    @Autowired private ZoutViewContoller zoutViewContoller;
    @Autowired private ReportManager reportManager;
    @Autowired private SessionManager sessionManager;
    @Autowired private ShortcutManager shortcutManager;
    @Autowired private ReceiptManager receiptManager;
    @Autowired private ZoutService zoutService;
    
    @FXML private Button buttonZoutReport;    
    private Label sessionClosedView;
    
    private boolean showingReport = false;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        
//        Platform.setImplicitExit(false);
//        
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
////                System.out.println("OnCloseRequest -> Stage Visible: " + primaryStage.isShowing());
////                if (!receiptManager.isCurrentReceiptEmpty()) {
////                    //primaryStage.show();
////                    //Dialog.showMessageDialog(getView().getScene(), "Error", "You cannot close application while on an incomplete receipt.");
////                    event.consume();
////                    primaryStage.show();
////                }
//                event.consume();
//            }
//        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sessionClosedView = new Label("Please open the business day to start transactions.");
        sessionClosedView.getStyleClass().add("session_message");
        sessionClosedView.setAlignment(Pos.CENTER);
        sessionClosedView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // setup shortcuts
        shortcutManager.setShortcutListener(Shortcut.XOUT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                doZout(null);
            }
        });
        shortcutManager.setShortcutListener(Shortcut.OPEN_CLOSE, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                doOpenClose(null);
            }
        });
        shortcutManager.setShortcutListener(Shortcut.LOGOUT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                logout(null);
            }
        });
        
        shortcutManager.setShortcutListener(Shortcut.EXIT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                if (!receiptManager.isCurrentReceiptEmpty()) {
                    Dialog.showMessageDialog(getView().getScene(), "Error", "You cannot close application while on an incomplete receipt.");
                    return;
                }
                
                Platform.exit();
            }
        });
    }
    
    public void loggedIn() {  
        BorderPane pane = (BorderPane) getView();        
        pane.setPrefSize(primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        
        if (reportManager.isDayOpened()) {
            // already opened.
            swicthIcon(false);
            pane.setCenter(receiptViewController.getView());
        } else {
            // closed, we need to open one to continue.
            swicthIcon(true);
            pane.setCenter(sessionClosedView);
        }
    }
    
    @FXML
    public void logout(ActionEvent ae) {       
        if (!receiptManager.isCurrentReceiptEmpty()) {
            Dialog.showMessageDialog(getView().getScene(), "Error", "You cannot logout while on an incomplete receipt.");
            return;
        }
        
        Scene scene = primaryStage.getScene();
        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().clear();
        root.getChildren().add(loginViewController.getView());
        loginViewController.focus();
        
        primaryStage.centerOnScreen();
    }
    
    @FXML
    public void doOpenClose(ActionEvent ae) {           
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.ZOUT_ACCESS_REGISTER_OPEN_CLOSE)) {
            AuthorizationDialog.show();
            return;
        }
        
        if (!reportManager.isDayOpened()) {
            openDialogViewController.show(getView().getScene(), new DialogListener() {
                @Override
                public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                    if (buttonPressed == Dialog.ButtonType.OK) {
                        reportManager.openDay(new BigDecimal(openDialogViewController.getOpeningBalance()));

                        BorderPane pane = (BorderPane) getView();
                        pane.setCenter(receiptViewController.getView());
                        swicthIcon(false);
                    }
                }
            });
        } else {
            if (!receiptManager.isCurrentReceiptEmpty()) {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "Please complete the sales transaction or cancel the receipt first.");
            }
            
            closeDialogViewController.setOpenedDate(reportManager.getPeriodBegin());
            closeDialogViewController.setTerminalName(sessionManager.getTerminal().getTerminalName());
            closeDialogViewController.show(getView().getScene(), new DialogListener() {
                @Override
                public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                    if (buttonPressed == Dialog.ButtonType.OK) {
                        reportManager.closeDay(new BigDecimal(closeDialogViewController.getDepositAmount()),
                                new BigDecimal(closeDialogViewController.getLeaveAmount()),
                                closeDialogViewController.getCurrencyInfo());

                        BorderPane pane = (BorderPane) getView();
                        pane.setCenter(sessionClosedView);
                        swicthIcon(true);
                    }
                }
            });
        }
    }
    
    @FXML
    public void doZout(ActionEvent ae) {           
        // PERM
        if (!sessionManager.getUserPermissions().hasPermission(UserPermissionsDto.XOUT_ACCESS_XOUT)) {
            AuthorizationDialog.show();
            return;
        }

        BorderPane pane = (BorderPane) getView();

        if (showingReport) {
            showingReport = false;
            if (reportManager.isDayOpened()) {
                pane.setCenter(receiptViewController.getView());
                receiptViewController.showSearchItemView(null);
            } else {
                pane.setCenter(sessionClosedView);
            }
        } else {
            if (receiptManager.isCurrentReceiptEmpty()) {
                ZoutDto lastReport = zoutService.findLastReport();
                if (lastReport != null && lastReport.getOpenInvcSid() != null) {
                    showingReport = true;        
                    zoutViewContoller.showReport();
                    pane.setCenter(zoutViewContoller.getView());
                } else {
                    Dialog.showMessageDialog(
                            getView().getScene(),
                            "Invalid Action",
                            "There are no sales yet to take report.");
                }
            } else {
                Dialog.showMessageDialog(
                        getView().getScene(),
                        "Invalid Action",
                        "Please complete the sales transaction or cancel the receipt first.");
            }            
        }
    }
    
    private void swicthIcon(boolean isZout) {
        ObservableList<String> styleClass = buttonZoutReport.getStyleClass();
        if (isZout) {
            int index = styleClass.indexOf("icon-xout");
            if (index != -1) {
                styleClass.remove(index);
                styleClass.add("icon-zout");
            }
        } else {
            int index = styleClass.indexOf("icon-zout");
            if (index != -1) {
                styleClass.remove(index);
                styleClass.add("icon-xout");
            }
        }
    }
}
