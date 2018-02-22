/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.manager.ReceiptManager;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author jomit
 */
public class ReceiptViewListManager {
    private ReceiptManager receiptManager;
    
    private ObservableList<ReceiptRow> receiptList;
    
    private ReceiptCustomRow discountRow;
    private ReceiptCustomRow taxAmountRow;
    private ReceiptCustomRow subtotalRow;
    private ReceiptCustomRow totalRow;

    public ReceiptViewListManager(ReceiptManager receiptManager) { 
        this.receiptManager = receiptManager;
        
        taxAmountRow = new ReceiptCustomRow("Tax", receiptManager.taxAmountProperty());
        subtotalRow  = new ReceiptCustomRow("Total", receiptManager.subtotalProperty());
        discountRow  = new ReceiptCustomRow("Discount", receiptManager.globalDiscountAmountProperty());
        totalRow     = new ReceiptCustomRow("Balance", receiptManager.totalProperty());
                
        receiptManager.currentItemsProperty().addListener(new ListChangeListener<ReceiptItemDto>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReceiptItemDto> c) {
                resetList();
            }
        });
        
        receiptManager.currentTendersProperty().addListener(new ListChangeListener<ReceiptTenderDto>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ReceiptTenderDto> c) {
                resetList();
            }
        });
        
        receiptManager.globalDiscountAmountProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue, BigDecimal newValue) {
                if (newValue.equals(BigDecimal.ZERO)) {
                    if (receiptList.contains(discountRow)) {
                        resetList();
                    }
                } else if (!receiptList.contains(discountRow)) {
                    resetList();
                }
            }
        });
        
        receiptList = FXCollections.observableArrayList();        
        resetList();
    }
    
    private void resetList() {
        receiptList.clear();
        
        int i = 1;
        if (!receiptManager.currentItemsProperty().isEmpty()) {
            for (ReceiptItemDto itemDto : receiptManager.currentItemsProperty()) {
                ReceiptItemRow row = new ReceiptItemRow(itemDto);
                row.setSerialNo(i);
                receiptList.add(row);                
                i++;
            }
           
            receiptList.add(subtotalRow);
            subtotalRow.setSerialNo(i);
            i++;
            
            receiptList.add(taxAmountRow);
            taxAmountRow.setSerialNo(i);
            i++;
            
            if (receiptManager.globalDiscountAmountProperty().get().doubleValue() > 0) {
                receiptList.add(discountRow);
                discountRow.setSerialNo(i);
                i++;
            }

            for (ReceiptTenderDto tenderDto : receiptManager.currentTendersProperty()) {
                ReceiptTenderRow row = new ReceiptTenderRow(tenderDto);
                receiptList.add(row);
                i++;
            }
            receiptList.add(totalRow);
            totalRow.setSerialNo(i);
        } else if (!receiptManager.currentTendersProperty().isEmpty()) {
            receiptList.add(subtotalRow);
            subtotalRow.setSerialNo(i);
            i++;
            receiptList.add(taxAmountRow);
            taxAmountRow.setSerialNo(i);
            i++;
            
            if (receiptManager.globalDiscountAmountProperty().get().doubleValue() > 0) {
                receiptList.add(discountRow);
                discountRow.setSerialNo(i);
                i++;
            }

            for (ReceiptTenderDto tenderDto : receiptManager.currentTendersProperty()) {
                ReceiptTenderRow row = new ReceiptTenderRow(tenderDto);
                receiptList.add(row);
                i++;
            }
            receiptList.add(totalRow);
            totalRow.setSerialNo(i);
        }
    }
    
    public ObservableList<ReceiptRow> receiptListProperty() {
        return receiptList;
    }
}
