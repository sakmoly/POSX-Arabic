/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.base;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author jomit
 */
public final class Dialog {
    private FlowPane dialogWrapper;
    private BorderPane dialogContainer;
    private HBox titleContainer; 
    private HBox buttonContainer; 
    private Label titleLabel;
    
    private ButtonType[] currentButtons;    
    private Scene currentScene;
    private StackPane currentStackPane;
    private int currentDialogIndex;    
    
    private DialogView currentView;
    private DialogListener currentListener;

    public Dialog() {
        initUI();
    }    
    
    private void initUI() {
        dialogWrapper   = new FlowPane();
        dialogContainer = new BorderPane();
        titleContainer  = new HBox();
        buttonContainer = new HBox();
        titleLabel      = new Label();
        currentDialogIndex = -1;
        
        dialogWrapper.setAlignment(Pos.CENTER);
        dialogWrapper.setColumnHalignment(HPos.CENTER);        
        dialogWrapper.getChildren().add(dialogContainer);
        dialogWrapper.getStyleClass().add("dialog");
        
        dialogContainer.setTop(titleContainer);
        dialogContainer.setBottom(buttonContainer);
        dialogContainer.setMinSize(300, 150);
        dialogContainer.getStyleClass().add("container");
        
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().add(titleLabel);
        titleContainer.getStyleClass().add("title");
        
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getStyleClass().add("options");
    }
    
    public void showDialog(Scene scene, String title, DialogView content, 
            DialogType dialogType, DialogListener listener, boolean hasDefault, boolean hasCancel) {  
        
        if (currentDialogIndex >= 0) {
            return;
        }
        
        currentScene    = scene;
        currentView     = content;
        currentListener = listener;
        Parent root     = scene.getRoot();
        titleLabel.setText(title);
        
        if (root instanceof StackPane) {
            currentStackPane = (StackPane) root;
        } else {
            currentStackPane = new StackPane();
            currentStackPane.getChildren().add(0, root);
        }
        currentDialogIndex = currentStackPane.getChildren().size();
        currentButtons     = dialogType.getButtons();
        
        buttonContainer.getChildren().clear();
        for (ButtonType buttonType : currentButtons) {
            buttonContainer.getChildren().add(buttonType.getButton());
            buttonType.getButton().setOnAction(new ButtonAction(buttonType));
        }
        
        BorderPane.setMargin(currentView.getView(), new Insets(20, 40, 20, 40));
        dialogContainer.setCenter(currentView.getView());
        currentStackPane.getChildren().add(currentDialogIndex, dialogWrapper);
        scene.setRoot(currentStackPane);
        
        currentView.getView().requestFocus();
        currentView.onBeforeShow();
        
        ButtonType.OK.getButton().setDefaultButton(hasDefault);
        ButtonType.CANCEL.getButton().setCancelButton(hasCancel);
    }
    
    private class ButtonAction implements EventHandler<ActionEvent> {
        private ButtonType button;

        public ButtonAction(ButtonType button) {
            this.button = button;
        }
        
        @Override
        public void handle(ActionEvent event) {
            hideCurrentDialog(button);
        }
    }
    
    private void hideCurrentDialog(final ButtonType button) {
        if (currentView == null) {
            return;
        }
        
        if (!currentView.onBeforeHide(button)) {
            return;
        }
        currentStackPane.getChildren().remove(currentDialogIndex);

        if (currentListener != null) {
            final DialogListener listener = currentListener;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    listener.dialogDismissed(button);
                }
            });
        }

        currentView = null;
        currentStackPane = null;
        currentListener = null;
        currentDialogIndex = -1;
    }
    
    public enum ButtonType {
        OK("OK"), CANCEL("Cancel"), YES("Yes"), NO("No"), PRINT("Print"), EXPORT("Export");
        
        private final String title;
        private final Button button;
        
        ButtonType(String title) {
            this.title  = title;
            this.button = new Button(title);
            this.button.setPrefWidth(100);
            this.button.setPrefHeight(40);
        }

        public final String getTitle() {
            return title;
        }

        public Button getButton() {
            return button;
        }
    }
    
    public enum DialogType {
        OK_ONLY(ButtonType.OK),
        OK_CANCEL(ButtonType.OK, ButtonType.CANCEL),
        YES_NO(ButtonType.YES, ButtonType.NO),
        YES_NO_CANCEL(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL),
        PRINT_EXPORT_CANCEL(ButtonType.PRINT, ButtonType.EXPORT, ButtonType.CANCEL);

        private final ButtonType[] buttons;
        
        DialogType(ButtonType ... buttons) {
            this.buttons = buttons;
        }

        public ButtonType[] getButtons() {
            return buttons;
        }
    }
    
    // -----------------------------------------------------------------------------------------------------------    
    private static final Dialog dialogInstance = new Dialog();
    private static final MessageDialogView messageDialogView = new MessageDialogView();
    
//    private static void setOptions(boolean hasDefault, boolean hasCancel) {
//        ButtonType.OK.getButton().setDefaultButton(hasDefault);
//        ButtonType.CANCEL.getButton().setCancelButton(hasCancel);
//    }
    
    public static void hide(ButtonType buttonType) {
        dialogInstance.hideCurrentDialog(buttonType);
    }
    
    public static void showMessageDialog(Scene scene, String title, String message) {
//        setOptions(true, true);
        messageDialogView.setMessage(message);
        dialogInstance.showDialog(scene, title, messageDialogView, DialogType.OK_ONLY, null, true, true);
    }
    
    public static void showMessageDialog(Scene scene, String title, String message, DialogType dialogType) {
//        setOptions(true, true);
        messageDialogView.setMessage(message);
        dialogInstance.showDialog(scene, title, messageDialogView, dialogType, null, true, true);
    }
    
    public static void showMessageDialog(Scene scene, String title, String message, DialogListener listener) {
//        setOptions(true, true);
        messageDialogView.setMessage(message);
        dialogInstance.showDialog(scene, title, messageDialogView, DialogType.OK_ONLY, listener, true, true);
    }
    
    public static void showMessageDialog(Scene scene, String title, String message, DialogType dialogType, DialogListener listener) {
//        setOptions(true, true);
        messageDialogView.setMessage(message);
        dialogInstance.showDialog(scene, title, messageDialogView, dialogType, listener, true, true);
    }
    
    public static void showCustomDialog(Scene scene, String title, DialogView dialogView, DialogListener listener) {
//        setOptions(true, true);
        dialogInstance.showDialog(scene, title, dialogView, DialogType.OK_ONLY, listener, true, true);
    }
    
    public static void showCustomDialog(Scene scene, String title, DialogView dialogView, DialogType dialogType, DialogListener listener) {
//        setOptions(true, true);
        dialogInstance.showDialog(scene, title, dialogView, dialogType, listener, true, true);
    }
    
    public static void showCustomDialog(Scene scene, String title, DialogView dialogView, DialogListener listener, boolean hasDefault, boolean hasCancel) {
//        setOptions(hasDefault, hasCancel);
        dialogInstance.showDialog(scene, title, dialogView, DialogType.OK_ONLY, listener, hasDefault, hasCancel);
    }
    
    public static void showCustomDialog(Scene scene, String title, DialogView dialogView, DialogType dialogType, DialogListener listener, boolean hasDefault, boolean hasCancel) {
//        setOptions(hasDefault, hasCancel);
        dialogInstance.showDialog(scene, title, dialogView, dialogType, listener, hasDefault, hasCancel);
    }
    
    private static class MessageDialogView implements DialogView {
        private Label messageLabel;

        public MessageDialogView() {
            this.messageLabel = new Label();
            this.messageLabel.setWrapText(true);
            this.messageLabel.setAlignment(Pos.CENTER);
            this.messageLabel.setTextAlignment(TextAlignment.CENTER);
            this.messageLabel.setPrefWidth(300);
            this.messageLabel.getStyleClass().add(".messageLabel");
        }
        
        public void setMessage(String message) {
            messageLabel.setText(message);
        }

        @Override
        public Node getView() { 
            return messageLabel; 
        }
        
        @Override
        public void onBeforeShow() { }
        @Override
        public boolean onBeforeHide(ButtonType button) { return true; }
        @Override
        public void setView(Node view) { }
        @Override
        public void initialize(URL location, ResourceBundle resources) { }
        @Override
        public void afterPropertiesSet() throws Exception { }
    }
}
