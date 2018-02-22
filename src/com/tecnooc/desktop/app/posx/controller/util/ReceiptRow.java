/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import java.math.BigDecimal;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author jomit
 */
public abstract class ReceiptRow {
    private SimpleStringProperty serialNo;

    public ReceiptRow() {
        serialNo = new SimpleStringProperty("");
    }
    
    public ReadOnlyStringProperty serialNoProperty() {
        return serialNo;
    }

    public String getSerialNo() {
        return serialNo.get();
    }

    public void setSerialNo(int serialNo) {
        this.serialNo.set(String.valueOf(serialNo));
    }
    
    public abstract ReadOnlyStringProperty  descriptionProperty();
    public abstract ReadOnlyObjectProperty<BigDecimal> quantityProperty();
    public abstract ReadOnlyObjectProperty<BigDecimal> discountProperty();
    public abstract ReadOnlyObjectProperty<BigDecimal> priceProperty();
    public abstract ReadOnlyObjectProperty<BigDecimal> taxAmountProperty();
    public abstract ReadOnlyObjectProperty<BigDecimal> totalProperty();
}
