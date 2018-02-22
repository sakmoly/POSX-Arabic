package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Zout;
import com.tecnooc.desktop.app.posx.model.ZoutTender;
import java.math.BigDecimal;

/**
 *
 * @author jomit
 */
public class ZoutTenderDto extends Dto<ZoutTender> {
    public ZoutTenderDto(ZoutTender entity) {
        super(entity);
    }

    public ZoutTenderDto() {
        super(new ZoutTender());
    }
    
    public BigDecimal getAmount() {
        return entity.getAmount();
    }

    public void setAmount(BigDecimal amount) {
        entity.setAmount(amount);
    }

    public BigDecimal getActualAmount() {
        return entity.getActualAmount();
    }

    public void setActualAmount(BigDecimal actualAmount) {
        entity.setActualAmount(actualAmount);
    }

    public BigDecimal getAmountDiff() {
        return entity.getAmountDiff();
    }

    public void setAmountDiff(BigDecimal amountDiff) {
        entity.setAmountDiff(amountDiff);
    }

    public Zout getZoutSid() {
        return entity.getZoutSid();
    }

    public void setZoutSid(Zout zoutSid) {
        entity.setZoutSid(zoutSid);
    }
}
