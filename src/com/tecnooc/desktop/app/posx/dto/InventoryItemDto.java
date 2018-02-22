package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Inventory;
import java.math.BigDecimal;

/**
 *
 * @author jomit
 */
public class InventoryItemDto extends Dto<Inventory> {
    private InventoryNumberDto numberDto;
    
    public InventoryItemDto(Inventory entity) {
        super(entity);
    }

    @Override
    public String toString() {
        return String.format("%-40s\t%16.2f", 
                entity.getDescription1(),entity.getSellingPrice().doubleValue());
    }
    
    public Long getItemId() {
        return entity.getItemSid();
    }
    
    public String getItemName() {
        return entity.getDescription1();
    }
    
    public BigDecimal getPrice() {
        return entity.getSellingPrice();
    }
    
    public BigDecimal getCost() {
        return entity.getCost();
    }
    
    public String getTaxType() {
        return entity.getTaxType();
    }
    
    public BigDecimal getTaxRate() {
        return entity.getTaxRate();
    }

    public InventoryNumberDto getInventoryNumberDto() {
        return numberDto;
    }

    public void setInventoryNumberDto(InventoryNumberDto numberDto) {
        this.numberDto = numberDto;
    }
}
