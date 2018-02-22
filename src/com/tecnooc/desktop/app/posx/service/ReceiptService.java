/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.model.Invoice;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface ReceiptService {
    public void save(ReceiptDto receipt);
    public List<ReceiptDto> findHeldReceipts();
    public void remove(Long receiptSid);
    public List<ReceiptDto> findByReceiptSidBetween(Invoice start, Invoice end);
}
