/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.dto.InventoryNumberDto;
import com.tecnooc.desktop.app.posx.model.InventoryNumber;
import com.tecnooc.desktop.app.posx.repository.InventoryNumberRepository;
import com.tecnooc.desktop.app.posx.service.InventoryNumberService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class InventoryNumberServiceImpl implements InventoryNumberService {
    @Autowired
    private InventoryNumberRepository inventoryNumberRepository;
    
    private List<InventoryNumberDto> toInventoryNumberDto(List<InventoryNumber> entities) {
        ArrayList<InventoryNumberDto> items = new ArrayList<>();
        for (InventoryNumber inventory : entities) {
            items.add(new InventoryNumberDto(inventory));
        }
        return items;
    }

    private List<InventoryItemDto> toInventoryItemDto(List<InventoryNumber> entities) {
        ArrayList<InventoryItemDto> items = new ArrayList<>();
        for (InventoryNumber entity : entities) {
            items.add(new InventoryItemDto(entity.getInventory()));
        }
        return items;
    }

    @Override
    public List<InventoryItemDto> lookupInventoryItem(String number) {
        return toInventoryItemDto(inventoryNumberRepository.findBySerialLotNumberAndActiveTrue(number));
    }

    @Override
    public List<InventoryNumberDto> findByInventoryItem(InventoryItemDto itemDto) {
        return toInventoryNumberDto(inventoryNumberRepository.findByInventoryAndActiveTrue(itemDto.getEntity()));
    }
    
    @Override
    @Transactional(readOnly = false)
    public void save(InventoryNumberDto number) {
        inventoryNumberRepository.save(number.getEntity());
    }
}
