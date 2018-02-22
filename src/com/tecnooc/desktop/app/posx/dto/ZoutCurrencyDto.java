package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.ZoutCurrency;
import com.tecnooc.desktop.app.posx.model.ZoutCurrencyPK;
import java.math.BigDecimal;

/**
 *
 * @author jomit
 */
public class ZoutCurrencyDto extends Dto<ZoutCurrency> {
    public ZoutCurrencyDto(ZoutCurrency entity) {
        super(entity);
    }

    public ZoutCurrencyDto() {
        super(new ZoutCurrency());
        entity.setZoutCurrencyPK(new ZoutCurrencyPK());
    }
    
    public void setZoutSid(Long sid) {
        entity.getZoutCurrencyPK().setZoutSid(sid);
    }
    
    public void setCurrencyDenominationId(int id) {
        entity.getZoutCurrencyPK().setCrrencyDenomId(id);
    }
    
    public int getMultiplier() {
        return entity.getMultiplier();
    }
    
    public void setMultiplier(int multiplier) {
        entity.setMultiplier(multiplier);
    }
    
    public BigDecimal getAmount() {
        return entity.getAmount();
    }
    
    public void setAmount(BigDecimal amount) {
        entity.setAmount(amount);
    }
}
