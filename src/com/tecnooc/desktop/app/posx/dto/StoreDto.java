package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Store;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class StoreDto extends Dto<Store> {  
    private final StringProperty nameProperty;

    public StoreDto(Store entity) {
        super(entity);
        
        this.nameProperty = new SimpleStringProperty(entity, "storeName");
    }
    
    public ReadOnlyStringProperty storeNameProperty() {
        return nameProperty;
    }
    
    public int getSbusidiaryNumber() {
        return entity.getSbsNo();
    }

    @Override
    public String toString() {
        return entity.getStoreName();
    }
}
