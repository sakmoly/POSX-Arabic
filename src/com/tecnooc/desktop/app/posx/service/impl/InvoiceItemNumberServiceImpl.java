/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.model.InvoiceItemNumber;
import com.tecnooc.desktop.app.posx.repository.InvoiceItemNumberRepository;
import com.tecnooc.desktop.app.posx.service.InvoiceItemNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class InvoiceItemNumberServiceImpl implements InvoiceItemNumberService {
    
    @Autowired private InvoiceItemNumberRepository repo;

    @Override
    @Transactional(readOnly = false)
    public void insert(Long invcSid, Long itemSid, Long inventoryNumberSid) {
        InvoiceItemNumber entity = new InvoiceItemNumber();
        
        entity.setInvcSid(invcSid);
        entity.setItemSid(itemSid);
        entity.setInventoryNumberRef(inventoryNumberSid);
        
        entity = repo.save(entity);
    }
    
}
