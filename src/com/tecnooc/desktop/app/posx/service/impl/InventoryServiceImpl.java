package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.model.Inventory;
import com.tecnooc.desktop.app.posx.repository.InventoryRepository;
import com.tecnooc.desktop.app.posx.service.InventoryService;
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
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    
    private List<InventoryItemDto> toInventoryItemDto(List<Inventory> entities) {
        ArrayList<InventoryItemDto> items = new ArrayList<>();
        for (Inventory inventory : entities) {
            items.add(new InventoryItemDto(inventory));
        }
        return items;
    }
    
    @Override
    public List<InventoryItemDto> lookup(String text) {
        return toInventoryItemDto(inventoryRepository.lookup(text));
    }    
    
    @Override
    public List<InventoryItemDto> findAll() {
        return toInventoryItemDto(inventoryRepository.findAll());
    }  

    @Override
    public InventoryItemDto findItem(Long itemSid) {
        Inventory item = inventoryRepository.findOne(itemSid);
        return item == null ? null : new InventoryItemDto(item);
    }

    @Override
    public InventoryItemDto lookupByCode(String alu) {
        Inventory item = null;        
        List<Inventory> items = inventoryRepository.lookupWeighingItem(alu);
        if (!items.isEmpty()) {
            item = items.get(0);
        }
        return item == null ? null : new InventoryItemDto(item);
    }
}
