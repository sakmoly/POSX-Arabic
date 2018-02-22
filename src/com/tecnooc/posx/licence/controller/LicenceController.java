/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.controller;

import com.tecnooc.desktop.app.posx.PosxApp;
import com.tecnooc.desktop.app.posx.controller.base.ViewController;
import com.tecnooc.posx.licence.activation.LicenceInfo;
import com.tecnooc.posx.licence.activation.LicenceInfoRepository;
import com.tecnooc.posx.licence.file.FileHandler;
import com.tecnooc.posx.licence.key.KeyManager;
import com.tecnooc.posx.licence.key.SystemInfoNotFoundException;
import com.tecnooc.posx.licence.util.LicenceUtil;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author jomit
 */
public class LicenceController implements Initializable {
    private static PosxApp app;
    private static Stage stage;
    
    private FileHandler fileHandler;
    
    @FXML
    private TextArea systemCode;
    @FXML
    private TextArea productKey;
    @FXML
    private Button continueTrialButton;
    @FXML
    private Label trialMessage;
    
    public static void showActivationView(PosxApp app, Stage stage) throws IOException {
        LicenceController.app   = app;
        LicenceController.stage = stage;
        
        Parent root = FXMLLoader.load(app.getClass().getResource("/res/skin/basic/fxml/licence.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/res/skin/basic/css/main.css");

        stage.setResizable(false);
        stage.setTitle(app.getTitle());
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void writeSystemCode(ActionEvent event) {
        try {
            fileHandler.write(systemCode.getText());
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(null, "Error writing file.\n" + ex.getMessage());
        }        
    }
    
    @FXML
    private void readProductKey(ActionEvent event) {
        try {
            String key = fileHandler.read();
            
            if (key != null) {
                productKey.setText(key);
            }
        } catch (IOException ex) {
            String errorMessage = "ERROR: Cannot Open File\n\n" + ex.getMessage();
            productKey.setText(errorMessage);
        }
    }
    
    @FXML
    private void activate(ActionEvent event) {
        try {
            KeyManager manager = new KeyManager();
            if (manager.isValidKey(productKey.getText())) {
                LicenceInfoRepository repo = new LicenceInfoRepository(LicenceUtil.getApplicationDirectory());
                LicenceInfo info = repo.loadLicenceInfo();
                info.setActivated(true);
                info.setProductKey(productKey.getText());
                repo.saveLicenceInfo(info);
                
                app.showApp();
            } else {
                JOptionPane.showMessageDialog(null, "Given key is invalid...");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error checking key....");
        } 
    }
    
    @FXML
    private void continueTrial(ActionEvent event) {
        app.showApp();
    }
    
    @FXML
    private void exit(ActionEvent event) {
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileHandler = new FileHandler();
        
        KeyManager manager = new KeyManager();
        try {
            systemCode.setText(manager.generateSystemCode());
        } catch (Exception ex) {
            systemCode.setText("ERROR\n\n" + ex.getMessage());
        } 
        
        LicenceInfoRepository repo = new LicenceInfoRepository(LicenceUtil.getApplicationDirectory());
        LicenceInfo info = repo.loadLicenceInfo();
        
        if (info.isTrialExpired()) {
            continueTrialButton.setDisable(true);
            continueTrialButton.getStyleClass().add("disabled");
            trialMessage.setText("Your trial has been expired.\nPlease activate to continue using it.");
        } else {
            String message = "Thankyou for trying POSX. \n" +
                    "You can use this full-feature trial for 240 hours. \n\n" +
                    "Hours Remaining: " + info.getHoursRemaining();
            trialMessage.setText(message);
        }
    }    
}
