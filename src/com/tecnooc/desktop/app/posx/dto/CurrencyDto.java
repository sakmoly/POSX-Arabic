package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Currency;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class CurrencyDto extends Dto<Currency> {
    private final StringProperty abbreviationProperty;

    public CurrencyDto(Currency entity) {
        super(entity);
        
        this.abbreviationProperty = new SimpleStringProperty(entity, "currencyAbbr");
    }
    
    public ReadOnlyStringProperty abbreviationProperty() {
        return abbreviationProperty;
    }

    @Override
    public String toString() {
        return entity.getCurrencyAbbr();
    }
}
