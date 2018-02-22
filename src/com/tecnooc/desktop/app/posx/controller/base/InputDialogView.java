/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.base;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author jomit
 */
public class InputDialogView implements DialogView {
    private Label messageLabel;
    private TextField inputField;
    private VBox container;

    public InputDialogView() {
        container = new VBox();
        container.setSpacing(5);
        container.setPrefWidth(400);

        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("text-large");

        inputField = new TextField();
        inputField.getStyleClass().add("text-large");

        container.getChildren().add(messageLabel);
        container.getChildren().add(inputField);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    public String getInput() {
        return inputField.getText();
    }

    @Override
    public Node getView() {
        return container;
    }

    @Override
    public void onBeforeShow() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                inputField.requestFocus();
            }
        });
    }

    @Override
    public boolean onBeforeHide(Dialog.ButtonType button) {
        return true;
    }

    @Override
    public void setView(Node view) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
