/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.manager;

import com.tecnooc.desktop.app.posx.PosxApp;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.InputDialogView;
import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.dto.InventoryNumberDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.service.InventoryNumberService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class InventoryNumberHelper {
    private InventoryItemDto itemDto;
    private InventoryNumberDto numberDto;
    private String searchText;
    private boolean isValid;
    private ValidationListener callback;
    
    private boolean readNumberAgain;
    
    private List<InventoryNumberDto> availableNumbers;
    private List<InventoryNumberDto> currentlyUsedNumbers;
    private ReceiptItemDto receiptItemDto;
    
    @Autowired private InventoryNumberService numberService;
    @Autowired private SessionManager session;

    public void init(InventoryItemDto itemDto, String searchText, ReceiptItemDto receiptItemDto) {
        this.itemDto    = itemDto;
        this.searchText = searchText;
        this.numberDto  = null;
        this.isValid    = false;
        this.callback   = null;
        
        this.readNumberAgain  = false;
        this.availableNumbers = null;
        this.receiptItemDto   = receiptItemDto;
    }
    
    public interface ValidationListener {
        public void validationCompleted(InventoryItemDto itemDto, boolean isValid);
    }
    
    public void validate(ValidationListener callback) {
        this.callback = callback;
        
        availableNumbers = numberService.findByInventoryItem(itemDto);
        if (availableNumbers.isEmpty()) {
            callback.validationCompleted(itemDto, true);
            return;
        }

        if (receiptItemDto == null) {
            currentlyUsedNumbers = new ArrayList<>();
        } else {
            currentlyUsedNumbers = receiptItemDto.getInventoryNumbers();
        }
        
        InventoryNumberDto number = getNumberDto(searchText);
        readNumberAgain = false;
        if (number != null) {
            checkValidity(number);
            callback.validationCompleted(itemDto, isValid);
            return;
        }
        
        readNumberAgain = true;
        readInventoryNumber();
    }

    private boolean isAlreadyAdded(InventoryNumberDto dto) {
        return currentlyUsedNumbers.contains(dto);
    }
    
    private InventoryNumberDto getNumberDto(String serialLotNumber) {
        for (InventoryNumberDto number : availableNumbers) {
            if (number.getSerialLotNumber().equals(serialLotNumber)) {
                return number;
            }
        }
        
        return null;
    }

    private void checkValidity(InventoryNumberDto dto) {
        if (dto.isSerialNumber() && (dto.isSold() || isAlreadyAdded(dto))) {
            isValid = false;
            Dialog.showMessageDialog(
                PosxApp.getApplicationScene(), 
                "Invalid Serial Number", 
                "Item with serial number '" + dto.getSerialLotNumber() + "' is already sold.",
                new DialogListener() {
                    @Override
                    public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                        if (readNumberAgain) {
                            readInventoryNumber();
                        }
                    }
                }
            );
            return;
        }
        
        if (dto.isLotNumber() && dto.isExpired()) {
            isValid = false;
            if (session.getProductExpiryAction().equals("warn")) {
                isValid = true;
                callback.validationCompleted(itemDto, isValid);
                readNumberAgain = false;
            }
            
            Dialog.showMessageDialog(
                PosxApp.getApplicationScene(),
                "Invalid Lot Number",
                "Item with lot number '" + dto.getSerialLotNumber() + "' has gone past its expiry date.",
                new DialogListener() {
                    @Override
                    public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                        if (readNumberAgain) {
                            readInventoryNumber();
                        }
                    }
                }
            );
            return;
        }
        
        this.numberDto = dto;
        itemDto.setInventoryNumberDto(numberDto);
        isValid = true;
    }
    
    private void readInventoryNumber() {
        InputDialogView view = new InputDialogView();
        view.setMessage("Please enter serial/lot number for current item.");
        
        DialogListener listener = new InputDialogListener(view);
        
        Dialog.showCustomDialog(
            PosxApp.getApplicationScene(), 
            "Information Required", 
            view, 
            Dialog.DialogType.OK_CANCEL, 
            listener
        );
    }
    
    private class InputDialogListener implements DialogListener {
        private InputDialogView inputView;

        public InputDialogListener(InputDialogView inputView) {
            this.inputView = inputView;
        }

        @Override
        public void dialogDismissed(Dialog.ButtonType buttonPressed) {
            if (buttonPressed == Dialog.ButtonType.CANCEL) {
                isValid = false;
                callback.validationCompleted(itemDto, isValid);
                return;
            }

            InventoryNumberDto number = getNumberDto(inputView.getInput());
            if (number != null) {
                checkValidity(number);
                
                if (isValid) {
                    callback.validationCompleted(itemDto, isValid);
                }
            } else {
                Dialog.showMessageDialog(
                    PosxApp.getApplicationScene(),
                    "Invalid Serial/Lot Number",
                    "Given serial/lot number '" + inputView.getInput() + "' cannot be found.",
                    new DialogListener() {
                        @Override
                        public void dialogDismissed(Dialog.ButtonType buttonPressed) {
                            readInventoryNumber();
                        }
                    }
                );
            }
        }
    }
}
