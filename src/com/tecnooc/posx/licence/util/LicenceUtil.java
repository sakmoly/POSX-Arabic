/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.util;

import com.tecnooc.posx.licence.activation.LicenceInfo;
import com.tecnooc.posx.licence.activation.LicenceInfoRepository;
import com.tecnooc.posx.licence.activation.TrialTimeCounter;
import com.tecnooc.posx.licence.key.KeyManager;
import com.tecnooc.posx.licence.key.SystemInfoNotFoundException;
import java.io.File;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Time;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomit
 */
public class LicenceUtil {
    
    public static boolean isActivated() {
        LicenceInfoRepository repo = new LicenceInfoRepository(getApplicationDirectory());
        LicenceInfo info = repo.loadLicenceInfo();
        
        if (info.isActivated()) {
            KeyManager manager = new KeyManager();
            try {
                return manager.isValidKey(info.getProductKey());
            } catch (Exception ex) {
                return false;
            } 
        }
        
        return false;
    }
    
    public static File getApplicationDirectory() {
        try {
            File jarFile = new File(LicenceUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return jarFile.getParentFile();
        } catch (URISyntaxException ex) {
            System.out.println("Error obtaining application directory path.");
            System.out.println("Check to make sure that it doesn't contains illegal characters.");
            System.out.println(ex.getMessage());
            System.exit(1);
            return null;
        }
    }
    
    public static void scheduleTrialTimeCounter() {
        int fifteenMinutes = 1000 * 60 * 15;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TrialTimeCounter(), fifteenMinutes, fifteenMinutes);
    }
}
