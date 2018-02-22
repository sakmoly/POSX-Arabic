/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service;

import com.tecnooc.desktop.app.posx.model.ApplicationPreferenceValue;

/**
 *
 * @author jomit
 */
public interface ApplicationPreferenceValueService {
    public String LANG_ENGLISH = "english";
    public String LANG_ARABIC = "arabic";
    
    public String getPreferedLanguage();
    
    public String getWeighingItemPrefix();
    public int getWeighingItemCodeLength();
    public int getWeighingItemQuantityLength();
    public int getWeighingItemPriceLength();
    public String getNonWeighingItemPrefix();
    public int getNonWeighingItemCodeLength();
    public int getNonWeighingItemPriceLength();
    
    public String getProductExpiryAction();
}
