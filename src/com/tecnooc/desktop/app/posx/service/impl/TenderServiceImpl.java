/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.model.Tender;
import com.tecnooc.desktop.app.posx.repository.TenderRepository;
import com.tecnooc.desktop.app.posx.service.TenderService;
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
public class TenderServiceImpl implements TenderService {
    @Autowired private TenderRepository repository;
    
    @Override
    public List<Tender> findAllActiveTenders() {
        return repository.findByActiveTrue();
    }    
}
