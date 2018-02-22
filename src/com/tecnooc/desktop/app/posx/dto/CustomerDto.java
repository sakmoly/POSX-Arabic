package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Customer;
import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class CustomerDto extends Dto<Customer> {
    private final StringProperty idNameProperty;
    private final StringProperty fullNameProperty;
    private final IntegerProperty creditLimitProperty;
    private final IntegerProperty creditUsedProperty;
    private final IntegerProperty storeCreditProperty;
    private final IntegerProperty maxDiscPercProperty;

    public CustomerDto(Customer entity) {
        super(entity);

        // Since customer dto is read only, fullname not binding doesn't pose much problem.
        this.fullNameProperty = new SimpleStringProperty(entity.getFirstName() + " " + entity.getLastName());
        this.idNameProperty    = new SimpleStringProperty(entity.getCustId() + " - " + entity.getFirstName() + " " + entity.getLastName());
        
        this.creditLimitProperty = new SimpleIntegerProperty(entity.getCreditLimit());
        this.creditUsedProperty = new SimpleIntegerProperty(entity.getCreditUsed());
        this.storeCreditProperty = new SimpleIntegerProperty(entity.getStoreCredit());
        this.maxDiscPercProperty = new SimpleIntegerProperty(entity.getMaxDiscPerc());
    }
    
    public ReadOnlyStringProperty fullNameProperty() {
        return fullNameProperty;
    }
    
    public ReadOnlyStringProperty idNameProperty() {
        return idNameProperty;
    }
    
    public ReadOnlyIntegerProperty creditLimitProperty() {
        return creditLimitProperty;
    }
    
    public ReadOnlyIntegerProperty creditUsedProperty() {
        return creditUsedProperty;
    }
    
    public ReadOnlyIntegerProperty storeCreditProperty() {
        return storeCreditProperty;
    }
    
    public ReadOnlyIntegerProperty maxDiscountPercentageProperty() {
        return maxDiscPercProperty;
    }
    
    public Integer getStoreCreditAvailable() {
        return storeCreditProperty.get();
    }
    
    public Integer getCreditRemaining() {
        return creditLimitProperty.get() - creditUsedProperty.get();
    }
    
    public Integer getCreditUsed() {
        return creditUsedProperty.get();
    }
    
    public void useStoreCredit(Integer amount) {
        int remainingCredit = getStoreCreditAvailable() - amount;
        storeCreditProperty.set(remainingCredit);
        entity.setStoreCredit(remainingCredit);
    }
    
    public void useCredit(int amount) {
        int usedCredit = creditUsedProperty.get() + amount;
        creditUsedProperty.set(usedCredit);
        entity.setCreditUsed(usedCredit);
    }

    public void setStoreCredit(int storeCredit) {        
        storeCreditProperty.set(storeCredit);
        entity.setStoreCredit(storeCredit);
    }
    
    public void setCreditRemaining(int amount) {
        int usedCredit = creditLimitProperty.get() - amount;
        creditUsedProperty.set(usedCredit);
        entity.setCreditUsed(usedCredit);
    }
    
    public boolean isDefaultCustomer() {
        return entity.getCustSid() == 0;
    }

    @Override
    public String toString() {
        return fullNameProperty.get();
    }    
}
