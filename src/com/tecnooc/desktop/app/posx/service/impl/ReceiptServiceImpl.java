/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.model.Invoice;
import com.tecnooc.desktop.app.posx.repository.InvoiceItemRepository;
import com.tecnooc.desktop.app.posx.repository.InvoiceRepository;
import com.tecnooc.desktop.app.posx.repository.InvoiceTenderRepository;
import com.tecnooc.desktop.app.posx.service.ReceiptService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class ReceiptServiceImpl implements ReceiptService {
    @Autowired private InvoiceRepository invoiceRepo;
    @Autowired private InvoiceItemRepository invoiceItemRepo;
    @Autowired private InvoiceTenderRepository invoiceTenderRepo;

    @Override
    @Transactional(readOnly = false)
    public void save(ReceiptDto receipt) {
        invoiceRepo.save(receipt.getEntity());
    }

    @Override
    public List<ReceiptDto> findHeldReceipts() {
        List<ReceiptDto> list = new ArrayList<>();
        for (Invoice invoice : invoiceRepo.findByHeldTrue()) {
            fillInvoiceDetails(invoice);
            list.add(new ReceiptDto(invoice));
        }
        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(Long receiptSid) {
        invoiceRepo.delete(receiptSid);
    }

    @Override
    public List<ReceiptDto> findByReceiptSidBetween(Invoice start, Invoice end) {
        List<ReceiptDto> list = new ArrayList<>();
        
        if (start == null || end == null) {
            return list;
        }
        
        System.out.println("-------------Start");
        for (Invoice invoice : invoiceRepo.findByHeldFalseAndInvcSidBetween(start.getInvcSid(), end.getInvcSid())) {
        //for (Invoice invoice : invoiceRepo.findByInvcSidGreaterThanAndInvcSidLessThan(start.getInvcSid() - 1, end.getInvcSid() + 1)) {
            fillInvoiceDetails(invoice);
            list.add(new ReceiptDto(invoice));
        }
        System.out.println("-------------End");
        return list;
    }
    
    private void fillInvoiceDetails(Invoice invoice) {
        invoice.setInvoiceItemsList(invoiceItemRepo.findByInvoice(invoice));
        invoice.setInvoiceTenderList(invoiceTenderRepo.findByInvcSid(invoice));
    }
}
