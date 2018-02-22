package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.StoreDto;
import com.tecnooc.desktop.app.posx.model.Store;
import com.tecnooc.desktop.app.posx.repository.StoreRepository;
import com.tecnooc.desktop.app.posx.service.StoreService;
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
public class StoreServiceImpl implements StoreService {    
    @Autowired
    private StoreRepository repository;
    
    @Override
    public StoreDto getStore() {
        List<Store> stores = repository.findAll();
        return stores.isEmpty()? null: new StoreDto(stores.get(0));
    }
}
