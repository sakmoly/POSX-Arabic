package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Invoice;
import com.tecnooc.desktop.app.posx.model.Store;
import com.tecnooc.desktop.app.posx.model.Terminal;
import com.tecnooc.desktop.app.posx.model.User;
import com.tecnooc.desktop.app.posx.model.Zout;
import com.tecnooc.desktop.app.posx.model.ZoutCurrency;
import com.tecnooc.desktop.app.posx.model.ZoutTender;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jomit
 */
public class ZoutDto extends Dto<Zout> {
    public ZoutDto(Zout entity) {
        super(entity);
    }

    public ZoutDto() {
        super(new Zout());
    }

    public void setZoutSid(Long zoutSid) {
        entity.setZoutSid(zoutSid);
    }

    public void setSbsNo(int sbsNo) {
        entity.setSbsNo(sbsNo);
    }

    public Date getBusinessDay() {
        return entity.getBusinessDay();
    }

    public void setBusinessDay(Date businessDay) {
        entity.setBusinessDay(businessDay);
    }

    public Date getPeriodBegin() {
        return entity.getPeriodBegin();
    }

    public void setPeriodBegin(Date periodBegin) {
        entity.setPeriodBegin(periodBegin);
    }

    public Date getPeriodEnd() {
        return entity.getPeriodEnd();
    }

    public void setPeriodEnd(Date periodEnd) {
        entity.setPeriodEnd(periodEnd);
    }

    public BigDecimal getTenderTotalOpen() {
        return entity.getTenderTotalOpen();
    }

    public void setTenderTotalOpen(BigDecimal tenderTotalOpen) {
        entity.setTenderTotalOpen(tenderTotalOpen);
    }

    public BigDecimal getTenderTotalClose() {
        return entity.getTenderTotalClose();
    }

    public void setTenderTotalClose(BigDecimal tenderTotalClose) {
        entity.setTenderTotalClose(tenderTotalClose);
    }

    public BigDecimal getOverShortAmt() {
        return entity.getOverShortAmt();
    }

    public void setOverShortAmt(BigDecimal overShortAmt) {
        entity.setOverShortAmt(overShortAmt);
    }

    public BigDecimal getDrawerLeaveAmt() {
        return entity.getDrawerLeaveAmt();
    }

    public void setDrawerLeaveAmt(BigDecimal drawerLeaveAmt) {
        entity.setDrawerLeaveAmt(drawerLeaveAmt);
    }

    public BigDecimal getDepositAmt() {
        return entity.getDepositAmt();
    }

    public void setDepositAmt(BigDecimal depositAmt) {
        entity.setDepositAmt(depositAmt);
    }

    public Date getCreatedDate() {
        return entity.getCreatedDate();
    }

    public void setCreatedDate(Date createdDate) {
        entity.setCreatedDate(createdDate);
    }

    public Date getModifiedDate() {
        return entity.getModifiedDate();
    }

    public void setModifiedDate(Date modifiedDate) {
        entity.setModifiedDate(modifiedDate);
    }

    public Integer getRetryCount() {
        return entity.getRetryCount();
    }

    public void setRetryCount(Integer retryCount) {
        entity.setRetryCount(retryCount);
    }

    public String getComputerName() {
        return entity.getComputerName();
    }

    public void setComputerName(String computerName) {
        entity.setComputerName(computerName);
    }

    public Integer getProcStatus() {
        return entity.getProcStatus();
    }

    public void setProcStatus(Integer procStatus) {
        entity.setProcStatus(procStatus);
    }

    public List<ZoutCurrencyDto> getZoutCurrencyList() {
        List<ZoutCurrencyDto> list = new ArrayList<>();
        
        if (entity.getZoutCurrencyList() == null) {
            return list;
        }
        
        for (ZoutCurrency zoutCurrency : entity.getZoutCurrencyList()) {
            list.add(new ZoutCurrencyDto(zoutCurrency));
        }
        return list;
    }

    public void setZoutCurrencyList(List<ZoutCurrencyDto> zoutCurrencyList) {
        List<ZoutCurrency> list = new ArrayList<>();
        for (ZoutCurrencyDto zoutCurrencyDto : zoutCurrencyList) {
            list.add(zoutCurrencyDto.getEntity());
        }
        entity.setZoutCurrencyList(list);
    }

    public List<ZoutTenderDto> getZoutTenderList() {        
        List<ZoutTenderDto> list = new ArrayList<>();
        
        if (entity.getZoutTenderList() == null) {
            return list;
        }
        
        for (ZoutTender zoutTender : entity.getZoutTenderList()) {
            list.add(new ZoutTenderDto(zoutTender));
        }
        return list;
    }

    public void setZoutTenderList(List<ZoutTenderDto> zoutTenderList) {
        List<ZoutTender> list = new ArrayList<>();
        for (ZoutTenderDto zoutTenderDto : zoutTenderList) {
            list.add(zoutTenderDto.getEntity());
        }
        entity.setZoutTenderList(list);
    }

    public User getUser() {
        return entity.getUserId();
    }

    public void setUser(User userId) {
        entity.setUserId(userId);
    }

    public Terminal getTerminal() {
        return entity.getTerminalId();
    }

    public void setTerminal(Terminal terminalId) {
        entity.setTerminalId(terminalId);
    }

    public Store getStore() {
        return entity.getStoreNo();
    }

    public void setStore(Store storeNo) {
        entity.setStoreNo(storeNo);
    }

    public Invoice getOpenInvcSid() {
        return entity.getOpenInvcSid();
    }

    public void setOpenInvcSid(Invoice openInvcSid) {
        entity.setOpenInvcSid(openInvcSid);
    }

    public Invoice getCloseInvcSid() {
        return entity.getCloseInvcSid();
    }

    public void setCloseInvcSid(Invoice closeInvcSid) {
        entity.setCloseInvcSid(closeInvcSid);
    }
}
