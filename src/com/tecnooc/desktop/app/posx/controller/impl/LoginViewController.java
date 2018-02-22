package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.AuthorizationDialog;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class LoginViewController extends AbstractViewController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusText;
    @FXML private Button loginButton;
    
    @Autowired private MainViewController mainViewController;
    @Autowired private SessionManager sessionManager;
    
    private Stage primaryStage;

    @Override
    public void afterPropertiesSet() throws Exception {
        //usernameField.setText("demo");
        //passwordField.setText("demo");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    @FXML
    public void login(ActionEvent ae) {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            return;
        }
        
        statusText.setText("Verifying...");
        
        if (sessionManager.login(usernameField.getText(), passwordField.getText())) {
            sessionManager.readPermissions();
            AuthorizationDialog.scene = getView().getScene();
            statusText.setText("Signing in...");
            loginButton.setDefaultButton(false);
            showMainViewController();
            passwordField.setText("");
        } else {
            statusText.getStyleClass().remove("normal");
            statusText.getStyleClass().add("error");
            statusText.setText("Invalid username or password!");
        }
    }
    
    private void showMainViewController() {
        mainViewController.setPrimaryStage(primaryStage);
        mainViewController.loggedIn();
        
        Scene scene = primaryStage.getScene();
        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().clear();
        root.getChildren().add(mainViewController.getView());
        
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();    
                
        statusText.getStyleClass().remove("error");
        statusText.getStyleClass().add("normal");
        statusText.setText("Enter Login Information");
    }

    void focus() {
        loginButton.setDefaultButton(true);
    }
}