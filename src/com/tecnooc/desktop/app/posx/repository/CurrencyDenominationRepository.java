package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.CurrencyDenomination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jomit
 */
@Repository
public interface CurrencyDenominationRepository  extends JpaRepository<CurrencyDenomination, Integer> {
    public List<CurrencyDenomination> findByActiveTrueOrderByMultiplierDesc();
}
