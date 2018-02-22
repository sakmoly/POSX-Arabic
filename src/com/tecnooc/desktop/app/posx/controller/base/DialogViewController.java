package com.tecnooc.desktop.app.posx.controller.base;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author jomit
 */
public abstract class DialogViewController extends AbstractViewController {
    private Stage dialogStage;
    private BorderPane dialogPane;    
    private Label titleLabel;
    
    @Override
    public void afterPropertiesSet() {        
        dialogPane = new BorderPane();
        dialogPane.getStyleClass().add("dialog");
        
        HBox header = new HBox();
        header.setSpacing(10);
        header.setPrefHeight(40);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header");
        
        titleLabel = new Label();
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setContentDisplay(ContentDisplay.CENTER);
        
        header.getChildren().add(titleLabel);
        dialogPane.setTop(header);
        
        HBox footer = new HBox();
        footer.setSpacing(10);
        footer.setPrefHeight(64);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("footer");
        
        Button okButton = new Button("OK");
        okButton.setPrefWidth(100);
        okButton.setPrefHeight(40);
        //okButton.setDefaultButton(true);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onOK();
            }
        });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(40);
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onCancel();
            }
        });
        
        footer.getChildren().add(okButton);
        footer.getChildren().add(cancelButton);
        dialogPane.setBottom(footer);   
        
        dialogPane.setCenter(getView());
        
        Scene scene = new Scene(dialogPane);
        scene.getStylesheets().add("/res/skin/basic/css/main.css");
        dialogStage = new Stage(StageStyle.UNDECORATED);

        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setIconified(false);
        dialogStage.centerOnScreen();
        
        dialogStage.setScene(scene);    
                
        onInit();
    }
    
    protected void onOK() {
        if (doOK()) {
            hide();
        }
    }
    
    protected void onCancel() {
        if (doCancel()) {
            hide();
        }
    }
    
    public void setDialogTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void hide() {
        onBeforeHide();
        dialogStage.hide();
    }
    
    public void show() {        
        onBeforeShow();
        dialogStage.show();
    }
    
    public void showAndWait() {
        onBeforeShow();
        dialogStage.showAndWait();
    }
    
    public void onInit() {
    }
    
    public boolean doOK() {
        return true;
    }
    
    public boolean doCancel() {
        return true;
    }
    
    public void onBeforeShow() {
    }
    
    public void onBeforeHide() {
    }
}
