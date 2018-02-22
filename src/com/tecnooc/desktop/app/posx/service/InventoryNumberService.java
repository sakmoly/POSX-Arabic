/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.dto.InventoryNumberDto;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface InventoryNumberService {
    public List<InventoryItemDto> lookupInventoryItem(String number);
    public List<InventoryNumberDto> findByInventoryItem(InventoryItemDto itemDto);
    public void save(InventoryNumberDto number);
}
