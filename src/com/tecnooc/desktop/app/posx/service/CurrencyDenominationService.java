package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.CurrencyDenominationDto;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface CurrencyDenominationService {
    public List<CurrencyDenominationDto> findDenominations();
}
