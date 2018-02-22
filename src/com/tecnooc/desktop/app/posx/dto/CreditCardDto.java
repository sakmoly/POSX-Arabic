package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.CreditCard;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class CreditCardDto extends Dto<CreditCard> {
    private final StringProperty nameProperty;

    public CreditCardDto(CreditCard entity) {
        super(entity);

        this.nameProperty = new SimpleStringProperty(entity, "cardName");
    }

    public ReadOnlyStringProperty cardNameProperty() {
        return nameProperty;
    }

    @Override
    public String toString() {
        return entity.getCardName();
    }
}
