/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.DialogView;
import com.tecnooc.desktop.app.posx.controller.base.DialogViewController;
import com.tecnooc.desktop.app.posx.dto.CurrencyDenominationDto;
import com.tecnooc.desktop.app.posx.dto.ZoutCurrencyDto;
import com.tecnooc.desktop.app.posx.service.CurrencyDenominationService;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class CloseDialogViewController extends AbstractViewController implements DialogView {
    @FXML private GridPane currencyGrid;
    @FXML private TextField leaveAmtField;
    @FXML private TextField depositAmtField;
    
    @FXML private Label openDateLabel;
    @FXML  private Label terminalnameLabel;
     
    @Autowired CurrencyDenominationService denominationService;
    
    private final int SPACING  = 8;
    private final int NUM_COLS = 6;
    
    private List<CurrencyDenominationDto> denominations;
    private List<TextField> denominationFields;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet(); 
        onInit();
    }

    public void onInit() {
        denominations = denominationService.findDenominations();
        denominationFields = new ArrayList<>();
        
        currencyGrid.setHgap(SPACING);
        currencyGrid.setVgap(SPACING);
        
        
        double percentageWidth = 100.0 / NUM_COLS;

        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(percentageWidth);
            constraints.setHgrow(Priority.NEVER);
            currencyGrid.getColumnConstraints().add(constraints);
        }
        
        int row = 0, col = 0;
        for (CurrencyDenominationDto denomination : denominations) {            
            currencyGrid.add(new Label(denomination.getDenominationName()), col, row);
            TextField field = new TextField();            
            currencyGrid.setHgrow(field, Priority.NEVER);
            field.setPrefWidth(60);
            field.setPrefHeight(32);
            field.setAlignment(Pos.CENTER_RIGHT);
            denominationFields.add(field);
            currencyGrid.add(field, col, row + 1);
            
            col++;
            if (col >= NUM_COLS) {
                col = 0;
                row += 2;
            }
        }
        
        if (denominationFields.size() > 0) {
            denominationFields.get(0).requestFocus();
        }
    }

    public void setOpenedDate(Date openedDate) {
        openDateLabel.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(openedDate));
    }

    public void setTerminalName(String terminalName) {
        terminalnameLabel.setText(terminalName);
    }
    
    public List<ZoutCurrencyDto> getCurrencyInfo() {
        List<ZoutCurrencyDto> list = new ArrayList<>();
        
        int i = 0;
        for (CurrencyDenominationDto denomination : denominations) {
            list.add(getCurrencyDto(denomination.getDenominationId(), denominationFields.get(i), denomination.getMultiplier().doubleValue()));
            i++;
        }  
        
        return list;
    }
    
    private ZoutCurrencyDto getCurrencyDto(int denomination, TextField multiplierField, double value) {
        ZoutCurrencyDto dto = new ZoutCurrencyDto();
        
        dto.setCurrencyDenominationId(denomination);
        int multiplier;
        try {
            multiplier = Integer.parseInt(multiplierField.getText());
        } catch (NumberFormatException ex) {
            multiplier = 0;
        }
        dto.setMultiplier(multiplier);
        dto.setAmount(new BigDecimal(value * multiplier));
        
        return dto;
    }
    
    public double getLeaveAmount() {
        try {
            return Double.parseDouble(leaveAmtField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    public double getDepositAmount() {
        try {
            return Double.parseDouble(depositAmtField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    public void show(Scene currentScene, DialogListener listener) {
        Dialog.showCustomDialog(currentScene, "Close Day", this, Dialog.DialogType.OK_CANCEL, listener);
    }

    @Override
    public void onBeforeShow() {
        leaveAmtField.setText("");
        depositAmtField.setText("");
        for (TextField textField : denominationFields) {
            textField.setText("");
        }
    }

    @Override
    public boolean onBeforeHide(Dialog.ButtonType button) { return true; }
}
