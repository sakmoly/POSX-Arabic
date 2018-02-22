/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.model.CreditCard;
import com.tecnooc.desktop.app.posx.repository.CreditCardRepository;
import com.tecnooc.desktop.app.posx.service.CreditCardService;
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
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired CreditCardRepository repository;
    
    @Override
    public List<CreditCard> findAllActiveCards() {
        return repository.findByActiveTrue();
    }    
}
