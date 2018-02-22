/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

/**
 *
 * @author jomit
 */
public class ReceiptItemRow extends ReceiptRow {
    private ReceiptItemDto receiptItem;

    public ReceiptItemRow(ReceiptItemDto receiptItem) {
        this.receiptItem = receiptItem;
    }

    public ReceiptItemDto getReceiptItem() {
        return receiptItem;
    }

    @Override
    public ReadOnlyStringProperty descriptionProperty() {
        return receiptItem.itemNameProperty();
    }

    @Override
    public ReadOnlyObjectProperty<BigDecimal> quantityProperty() {
        return receiptItem.quantityProperty();
    }

    @Override
    public ReadOnlyObjectProperty<BigDecimal> discountProperty() {
        return receiptItem.discountAmountProperty();
    }

    @Override
    public ReadOnlyObjectProperty<BigDecimal> priceProperty() {
        return receiptItem.sellingPriceProperty();
    }
    
    @Override
    public ReadOnlyObjectProperty<BigDecimal> taxAmountProperty() {
        return receiptItem.taxAmountProperty();
    }

    @Override
    public ReadOnlyObjectProperty<BigDecimal> totalProperty() {
        return receiptItem.totalPriceProperty();
    }

    @Override
    public String toString() {
        String format = "%-2s %-20s %4d %6.2f %8.2f %8.2f %8.2f";
        return String.format(format, serialNoProperty().get(), descriptionProperty().get(),
                quantityProperty().get(), discountProperty().get().doubleValue(), priceProperty().get().doubleValue(),
                taxAmountProperty().get().doubleValue(), totalProperty().get().doubleValue());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.receiptItem);
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
        final ReceiptItemRow other = (ReceiptItemRow) obj;
        if (!Objects.equals(this.receiptItem, other.receiptItem)) {
            return false;
        }
        return true;
    }
}
