/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.model.Tender;
import java.util.List;

/**
 *
 * @author jomit
 */
public interface TenderService {
    public List<Tender> findAllActiveTenders();
}
