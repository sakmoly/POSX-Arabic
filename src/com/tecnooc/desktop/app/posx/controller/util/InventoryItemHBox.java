package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import java.math.BigDecimal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 *
 * @author jomit
 */
public class InventoryItemHBox extends HBox {
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    
    public void setName(String name) {
        nameLabel.setText(name);
    }
    
    public void setPrice(BigDecimal price) {
        if (price == null) {
            priceLabel.setText(null);
        } else {
            priceLabel.setText(String.format("%.2f", price.doubleValue()));
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            nameLabel.getStyleClass().add("selected");
            priceLabel.getStyleClass().add("selected");
        } else {
            nameLabel.getStyleClass().remove("selected");
            priceLabel.getStyleClass().remove("selected");
        }
    }
}
