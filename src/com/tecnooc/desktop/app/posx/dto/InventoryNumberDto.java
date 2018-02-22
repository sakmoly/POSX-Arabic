package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.InventoryNumber;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jomit
 */
public class InventoryNumberDto extends Dto<InventoryNumber> {
    public InventoryNumberDto(InventoryNumber entity) {
        super(entity);
    }

    public String getSerialLotNumber() {
        return this.entity.getSerialLotNumber();
    }

    public boolean isSerialNumber() {
        Integer type = this.entity.getType();
        return type != null && type == 1;
    }
    
    public boolean isLotNumber() {
        Integer type = this.entity.getType();
        return type != null && type == 2;
    }

    public boolean isSold() {
        Integer sold = this.entity.getSerialSold();
        return sold != null && sold == 1;
    }
    
    public boolean isExpired() {
        Date expiryDate = this.entity.getLotExpiryDate();
        if (expiryDate == null) {
            return false;
        }
        
        try {
            // Get current date without time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today           = sdf.parse(sdf.format(new Date()));
            
            return expiryDate.before(today);
        } catch (ParseException ex) {
            return true;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InventoryNumberDto)) {
            return false;
        }

        InventoryNumberDto other = (InventoryNumberDto) obj;
        return entity.equals(other.entity);
    }

    @Override
    public int hashCode() {
        return entity != null ? entity.hashCode() : 0;
    }
}
