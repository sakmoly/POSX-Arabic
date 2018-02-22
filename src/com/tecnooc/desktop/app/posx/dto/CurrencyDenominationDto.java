package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.CurrencyDenomination;
import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class CurrencyDenominationDto extends Dto<CurrencyDenomination> {
    private final StringProperty nameProperty;
    private final ObjectProperty<BigDecimal> multiplierProperty;

    public CurrencyDenominationDto(CurrencyDenomination entity) {
        super(entity);
        
        this.nameProperty = new SimpleStringProperty(entity.getCurrencyDenomName());
        this.multiplierProperty = new SimpleObjectProperty<>(entity.getMultiplier());
    }
    
    public ReadOnlyStringProperty denominationNameProperty() {
        return nameProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> multiplierProperty() {
        return multiplierProperty;
    }
    
    public String getDenominationName() {
        return nameProperty.get();
    }
    
    public BigDecimal getMultiplier() {
        return multiplierProperty.get();
    }
    
    public int getDenominationId() {
        return entity.getCurrencyDenomId();
    }

    @Override
    public String toString() {
        return entity.getCurrencyDenomName();
    }
}
