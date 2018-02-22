/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author jomit
 */
public class ReceiptTenderRow extends ReceiptRow {
    private ReceiptTenderDto tender;
    
    private SimpleObjectProperty<BigDecimal> quantityProperty;
    private SimpleObjectProperty<BigDecimal> discountProperty;
    private SimpleObjectProperty<BigDecimal> priceProperty;
    private SimpleObjectProperty<BigDecimal> taxAmountProperty;
    
    private SimpleStringProperty nameProperty;

    public ReceiptTenderRow(ReceiptTenderDto tender) {
        this.tender = tender;
        
        //quantityProperty    = new SimpleIntegerProperty();
        nameProperty = new SimpleStringProperty("Taken (by " + tender.tenderNameProperty().get() + ")");
        discountProperty    = new SimpleObjectProperty<>();
        priceProperty       = new SimpleObjectProperty<>();
        taxAmountProperty   = new SimpleObjectProperty<>();
    }
    
    @Override
    public SimpleStringProperty descriptionProperty() {        
        return nameProperty;
    }

    @Override
    public SimpleObjectProperty<BigDecimal> quantityProperty() {
        return quantityProperty;
    }

    @Override
    public SimpleObjectProperty<BigDecimal> discountProperty() {
        return discountProperty;
    }

    @Override
    public SimpleObjectProperty<BigDecimal> priceProperty() {
        return priceProperty;
    }

    @Override
    public SimpleObjectProperty<BigDecimal> taxAmountProperty() {
        return taxAmountProperty;
    }

    @Override
    public ObjectProperty<BigDecimal> totalProperty() {
        return tender.amountProperty();
    }

    public ReceiptTenderDto getTender() {
        return tender;
    }

    @Override
    public String toString() {
        String format = "%-2s %-20s %4s %6s %8s %8.2f";
        return String.format(format, serialNoProperty().get(), descriptionProperty().get(),
                "", "", "",
                totalProperty().get().doubleValue());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.tender);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReceiptTenderRow other = (ReceiptTenderRow) obj;
        if (!Objects.equals(this.tender, other.tender)) {
            return false;
        }
        return true;
    }
}
