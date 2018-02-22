package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.dto.CurrencyDenominationDto;
import com.tecnooc.desktop.app.posx.model.CurrencyDenomination;
import com.tecnooc.desktop.app.posx.repository.CurrencyDenominationRepository;
import com.tecnooc.desktop.app.posx.service.CurrencyDenominationService;
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
public class CurrencyDenominationServiceImpl implements CurrencyDenominationService {
    @Autowired private CurrencyDenominationRepository repository;
    
    @Override
    public List<CurrencyDenominationDto> findDenominations() {
        List<CurrencyDenomination> denominations = repository.findByActiveTrueOrderByMultiplierDesc();
        List<CurrencyDenominationDto> list = new ArrayList<>();
        for (CurrencyDenomination currencyDenomination : denominations) {
            list.add(new CurrencyDenominationDto(currencyDenomination));
        }
        
        return list;
    }
    
}
