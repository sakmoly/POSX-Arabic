package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Invoice;
import com.tecnooc.desktop.app.posx.model.InvoiceItem;
import com.tecnooc.desktop.app.posx.model.InvoiceItemPK;
import com.tecnooc.desktop.app.posx.model.InvoiceTender;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jomit
 */
public class ReceiptDto extends Dto<Invoice> {
    public final int RECEIPT_SALES = 0;
    public final int RECEIPT_RETURN = 2;
    
    public final int STATUS_PENDING = 0;
    public final int STATUS_SENT = 1;
    
    public ReceiptDto(Invoice entity) {
        super(entity);
        
        entity.setBusinessDay(new Date());
    }
    
    public ReceiptDto() {
        super(new Invoice());
        
        Date now = new Date();
        entity.setBusinessDay(now);
        entity.setCreatedDate(now);
        entity.setModifiedDate(now);
        entity.setTimeTaken(now);
        
        entity.setInvcType(RECEIPT_SALES);
        entity.setProcStatus(STATUS_PENDING);
        entity.setRoundingOffset(BigDecimal.valueOf(2));
        entity.setHeld(false);
    }
    
    public Long getReceiptSid() {
        return entity.getInvcSid();
    }

    public final Integer getSubsidiaryNo() {
        return entity.getSbsNo();
    }

    public final Integer getInvoiceNo() {
        return entity.getInvcNo();
    }

    public final CustomerDto getCustomer() {
        return new CustomerDto(entity.getCustSid());
    }

    public final Integer getInvoiceType() {
        return entity.getInvcType();
    }

    public final BigDecimal getDiscountPercentage() {
        return entity.getDiscPerc();
    }

    public final BigDecimal getDiscountAmount() {
        return entity.getDiscAmt();
    }

    public final Boolean isHeld() {
        return entity.getHeld();
    }

    public final void setReceiptSid(Long receiptSid) {
        entity.setInvcSid(receiptSid);
        
        for (InvoiceItem invoiceItem : entity.getInvoiceItemsList()) {
            invoiceItem.setInvoiceItemsPK(new InvoiceItemPK(invoiceItem.getInventory().getItemSid(), receiptSid));
            invoiceItem.setInvoice(getEntity());
        }
        
        for (InvoiceTender invoiceTender : entity.getInvoiceTenderList()) {
            invoiceTender.setInvcSid(getEntity());
        }
    }

    public final void setSubsidiaryNo(Integer subsidiaryNo) {
        entity.setSbsNo(subsidiaryNo);
    }

    public final void setInvoiceNo(Integer invoiceNo) {
        entity.setInvcNo(invoiceNo);
    }

    public final void setStore(StoreDto store) {
        entity.setStoreNo(store.getEntity());
    }

    public final void setUser(UserDto user) {
        entity.setUserId(user.getEntity());
    }

    public final void setCustomer(CustomerDto customer) {
        entity.setCustSid(customer.getEntity());
    }

    public final void setInvoiceType(Integer invoiceType) {
        entity.setInvcType(invoiceType);
    }

    public final void setDiscountPercentage(BigDecimal discountPercentage) {
        entity.setDiscPerc(discountPercentage);
    }

    public final void setDiscountAmount(BigDecimal discountAmount) {
        entity.setDiscAmt(discountAmount);
    }

    public final void setDiscountPercentageSpread(BigDecimal discountPercentageSpread) {
        entity.setDiscPercSpread(discountPercentageSpread);
    }

    public final void setHeld(Boolean held) {
        entity.setHeld(held);
    }
    
    public final void setModifiedDate(Date date) {
        entity.setModifiedDate(date);
    }

    public final ObservableList<ReceiptItemDto> getItemsList() {
        ObservableList<ReceiptItemDto> list = FXCollections.observableArrayList();
        for (InvoiceItem invoiceItem : entity.getInvoiceItemsList()) {
            list.add(new ReceiptItemDto(invoiceItem));
        }
        return list;
    }

    public final void setItemsList(ObservableList<ReceiptItemDto> itemsList) {
        List<InvoiceItem> list = new ArrayList<>();
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (ReceiptItemDto itemDto : itemsList) {
            list.add(itemDto.getEntity());
            taxAmount = taxAmount.add(itemDto.getTaxAmount());
        }
        
        entity.setInvoiceItemsList(list);
        entity.setTotalTaxAmount(taxAmount);
    }

    public final ObservableList<ReceiptTenderDto> getTenderList() {
        ObservableList<ReceiptTenderDto> list = FXCollections.observableArrayList();
        for (InvoiceTender invoiceTender : entity.getInvoiceTenderList()) {
            list.add(new ReceiptTenderDto(invoiceTender));
        }
        return list;
    }

    public final void setTenderList(ObservableList<ReceiptTenderDto> tenderList) {
        List<InvoiceTender> list = new ArrayList<>();
        for (ReceiptTenderDto tenderDto : tenderList) {
            list.add(tenderDto.getEntity());
        }
        
        entity.setInvoiceTenderList(list);
    }

    @Override
    public String toString() {
        return String.format("%s\t%s", 
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(entity.getCreatedDate()), 
                entity.getCustSid().getFirstName() + " " + entity.getCustSid().getLastName());
    }
}
