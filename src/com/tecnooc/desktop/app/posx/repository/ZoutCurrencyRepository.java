/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.desktop.app.posx.repository;

import com.tecnooc.desktop.app.posx.model.Zout;
import com.tecnooc.desktop.app.posx.model.ZoutCurrency;
import com.tecnooc.desktop.app.posx.model.ZoutCurrencyPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jomit
 */
public interface ZoutCurrencyRepository extends JpaRepository<ZoutCurrency, ZoutCurrencyPK> {
    public List<ZoutCurrency> findByZout(Zout zout);
}
