package com.tecnooc.desktop.app.posx.manager;

import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.dto.ZoutCurrencyDto;
import com.tecnooc.desktop.app.posx.dto.ZoutDto;
import com.tecnooc.desktop.app.posx.dto.ZoutTenderDto;
import com.tecnooc.desktop.app.posx.service.ReceiptService;
import com.tecnooc.desktop.app.posx.service.ZoutService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class ReportManager implements InitializingBean { 
    private final Random random = new Random();
    
    @Autowired private ZoutService zoutService;
    @Autowired private ReceiptService receiptService;
    @Autowired private SessionManager sessionManager;
    
    @Override
    public void afterPropertiesSet() throws Exception {
    }
    
    public Date getPeriodBegin() {
        ZoutDto lastReport = zoutService.findLastReportWithoutCurrencyList();
        return lastReport.getPeriodBegin();
    }
    
    public boolean isDayOpened() {
        ZoutDto lastReport = zoutService.findLastReportWithoutCurrencyList();
        
        if (lastReport == null) {
            return false;
        }
        
        return lastReport.getTenderTotalClose() == null;
    }
    
    public void openDay(BigDecimal initialbalance) {
        ZoutDto zout = new ZoutDto();
        
        zout.setZoutSid(Util.generateSid());
        zout.setSbsNo(random.nextInt(100000));
        zout.setStore(sessionManager.getStore().getEntity());
        zout.setTerminal(sessionManager.getTerminal().getEntity());
        zout.setUser(sessionManager.getUser().getEntity());
        Date now = new Date();
        zout.setBusinessDay(now);
        zout.setPeriodBegin(now);
        zout.setCreatedDate(now);
        zout.setModifiedDate(now);
        zout.setTenderTotalOpen(initialbalance);
        
        //zoutService.save(zout);
        zoutService.saveNew(zout);
    }
    
    public void closeDay(BigDecimal depositAmount, BigDecimal leaveAmount, List<ZoutCurrencyDto> currencyList) {
        System.out.println("Starting close day...");
        System.out.println("Finding Last Report");
        ZoutDto zout = zoutService.findLastReport();
        
        System.out.println("Getting entity");
        zout.setUser(sessionManager.getUser().getEntity());
        Date now = new Date();
        zout.setModifiedDate(now);
        zout.setPeriodEnd(now);
        zout.setDepositAmt(depositAmount);
        zout.setDrawerLeaveAmt(leaveAmount);

        System.out.println("totalCash start");
        List<ReceiptDto> receipts = receiptService.findByReceiptSidBetween(zout.getOpenInvcSid(), zout.getCloseInvcSid());
        BigDecimal totalCash = BigDecimal.ZERO;
        for (ReceiptDto receiptDto : receipts) {
            for (ReceiptTenderDto tenderDto : receiptDto.getTenderList()) {
                if (tenderDto.getTenderType() == ReceiptTenderDto.TENDER_CASH) {
                    totalCash = totalCash.add(tenderDto.getAmount());
                }
            }
        }
        
        System.out.println("totalInDrawer start");
        BigDecimal totalInDrawer = BigDecimal.ZERO;
        for (ZoutCurrencyDto zoutCurrencyDto : currencyList) {
            zoutCurrencyDto.getEntity().getZoutCurrencyPK().setZoutSid(zout.getEntity().getZoutSid());
            totalInDrawer = totalInDrawer.add(zoutCurrencyDto.getAmount());
        }

        System.out.println("other start");
        BigDecimal overShortAmount = totalCash.subtract(totalInDrawer);

        zout.setTenderTotalClose(totalInDrawer);
        zout.setOverShortAmt(overShortAmount);
        zout.setZoutCurrencyList(currencyList);
        
        ZoutTenderDto zoutTenderDto = new ZoutTenderDto();
        zoutTenderDto.setZoutSid(zout.getEntity());
        zoutTenderDto.setAmount(totalInDrawer);
        zoutTenderDto.setActualAmount(totalCash);
        zoutTenderDto.setAmountDiff(overShortAmount);
        
        List<ZoutTenderDto> list = new ArrayList<>();
        list.add(zoutTenderDto);
        
        System.out.println("set tender list");
        zout.setZoutTenderList(list);
        
        System.out.println("save...");
        zoutService.save(zout);
        System.out.println("Ending close day....");
    }

    void updateInvoiceSid(ReceiptDto currentReceipt) {
        ZoutDto lastReport = zoutService.findLastReportWithoutCurrencyList();
        
        if (lastReport.getOpenInvcSid() == null) {
            lastReport.setOpenInvcSid(currentReceipt.getEntity());
        }
        
        lastReport.setCloseInvcSid(currentReceipt.getEntity());
        
        zoutService.updateInvoiceSid(lastReport);
    }
}
