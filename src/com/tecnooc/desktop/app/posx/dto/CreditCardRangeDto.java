package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.CreditCardRange;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class CreditCardRangeDto extends Dto<CreditCardRange> {
    private final StringProperty lowerRangeProperty;
    private final StringProperty upperRangeProperty;

    public CreditCardRangeDto(CreditCardRange entity) {
        super(entity);

        this.lowerRangeProperty = new SimpleStringProperty(entity, "lowerRange");
        this.upperRangeProperty = new SimpleStringProperty(entity, "upperRange");
    }

    public ReadOnlyStringProperty lowerRangeProperty() {
        return lowerRangeProperty;
    }

    public ReadOnlyStringProperty upperRangeProperty() {
        return upperRangeProperty;
    }  
}
