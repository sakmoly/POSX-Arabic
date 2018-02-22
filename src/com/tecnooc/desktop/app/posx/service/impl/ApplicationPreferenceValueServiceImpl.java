/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.service.impl;

import com.tecnooc.desktop.app.posx.model.ApplicationPreferenceValue;
import com.tecnooc.desktop.app.posx.repository.ApplicationPreferenceValueRepository;
import com.tecnooc.desktop.app.posx.service.ApplicationPreferenceValueService;
import static com.tecnooc.desktop.app.posx.service.ApplicationPreferenceValueService.LANG_ENGLISH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jomit
 */
@Service
@Transactional(readOnly = true)
public class ApplicationPreferenceValueServiceImpl implements ApplicationPreferenceValueService {
    @Autowired ApplicationPreferenceValueRepository repository;
    
    @Override
    public String getPreferedLanguage() {
        ApplicationPreferenceValue languagePreference = repository.findOne(1);
        if (languagePreference == null || languagePreference.getAppPrefValue() == null) {
            return LANG_ENGLISH;
        }
        
        return languagePreference.getAppPrefValue();
    }

    @Override
    public String getWeighingItemPrefix() {
        ApplicationPreferenceValue preference = repository.findOne(5);
        if (preference == null || preference.getAppPrefValue() == null) {
            return "";
        }

        return preference.getAppPrefValue();
    }

    @Override
    public int getWeighingItemCodeLength() {
        ApplicationPreferenceValue preference = repository.findOne(2);
        if (preference == null || preference.getAppPrefValue() == null) {
            return 0;
        }

        return Integer.parseInt(preference.getAppPrefValue());
    }

    @Override
    public int getWeighingItemQuantityLength() {
        ApplicationPreferenceValue preference = repository.findOne(3);
        if (preference == null || preference.getAppPrefValue() == null) {
            return 0;
        }

        return Integer.parseInt(preference.getAppPrefValue());
    }

    @Override
    public int getWeighingItemPriceLength() {
        ApplicationPreferenceValue preference = repository.findOne(4);
        if (preference == null || preference.getAppPrefValue() == null) {
            return 0;
        }

        return Integer.parseInt(preference.getAppPrefValue());
    }

    @Override
    public String getNonWeighingItemPrefix() {
        ApplicationPreferenceValue preference = repository.findOne(9);
        if (preference == null || preference.getAppPrefValue() == null) {
            return "";
        }

        return preference.getAppPrefValue();
    }

    @Override
    public int getNonWeighingItemCodeLength() {
        ApplicationPreferenceValue preference = repository.findOne(6);
        if (preference == null || preference.getAppPrefValue() == null) {
            return 0;
        }

        return Integer.parseInt(preference.getAppPrefValue());
    }

    @Override
    public int getNonWeighingItemPriceLength() {
        ApplicationPreferenceValue preference = repository.findOne(8);
        if (preference == null || preference.getAppPrefValue() == null) {
            return 0;
        }

        return Integer.parseInt(preference.getAppPrefValue());
    }

    @Override
    public String getProductExpiryAction() {
        ApplicationPreferenceValue preference = repository.findOne(10);
        if (preference == null || preference.getAppPrefValue() == null) {
            return "";
        }

        return preference.getAppPrefValue();
    }
}
