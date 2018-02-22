/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.dto.ZoutDto;

/**
 *
 * @author jomit
 */
public interface ZoutService {
    public ZoutDto findById(Long id);
    public ZoutDto findLastReport(); 
    public ZoutDto findLastReportWithoutCurrencyList(); 
    public void save(ZoutDto zout);
    public void saveNew(ZoutDto zout);
    public void updateInvoiceSid(ZoutDto zout);
}
