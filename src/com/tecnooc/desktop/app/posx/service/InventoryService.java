/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface InventoryService {
    public List<InventoryItemDto> lookup(String text);
    public InventoryItemDto lookupByCode(String alu);
    public List<InventoryItemDto> findAll();
    public InventoryItemDto findItem(Long itemSid);
}
