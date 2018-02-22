/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class ReceiptCustomRow extends ReceiptRow  {
    private StringProperty descriptionProperty;
    private ObjectProperty<BigDecimal> quantityProperty;
    private ObjectProperty<BigDecimal> discountProperty;
    private ObjectProperty<BigDecimal> priceProperty;
    private ObjectProperty<BigDecimal> taxAmountProperty;
    private ObjectProperty<BigDecimal> totalProperty;
    
    public ReceiptCustomRow(String description, ObjectProperty<BigDecimal> totalProperty) {
        descriptionProperty = new SimpleStringProperty(description);
        //quantityProperty    = new SimpleIntegerProperty();
        discountProperty    = new SimpleObjectProperty<>();
        priceProperty    = new SimpleObjectProperty<>();
        taxAmountProperty    = new SimpleObjectProperty<>();
        this.totalProperty  = totalProperty;
    }

    @Override
    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    @Override
    public ReadOnlyObjectProperty<BigDecimal> quantityProperty() {
        return quantityProperty;
    }

    @Override
    public ObjectProperty<BigDecimal> discountProperty() {
        return discountProperty;
    }

    @Override
    public ObjectProperty<BigDecimal> totalProperty() {
        return totalProperty;
    }

    @Override
    public ObjectProperty<BigDecimal> priceProperty() {
        return priceProperty;
    }

    @Override
    public ObjectProperty<BigDecimal> taxAmountProperty() {
        return taxAmountProperty;
    }

    @Override
    public String getSerialNo() {
        return "";
    }

    @Override
    public void setSerialNo(int serialNo) { }


    @Override
    public String toString() {
        String format = "%-2s %-40s %4s %6.2s %8s %8.2f";
        return String.format(format, serialNoProperty().get(), descriptionProperty().get(),
                "", "", "",
                totalProperty().get().doubleValue());
    }
}
