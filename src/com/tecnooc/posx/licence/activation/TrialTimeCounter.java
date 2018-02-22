/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.activation;

import com.tecnooc.posx.licence.util.LicenceUtil;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 *
 * @author jomit
 */
public class TrialTimeCounter extends TimerTask {
    @Override
    public void run() {
        LicenceInfoRepository repo = new LicenceInfoRepository(LicenceUtil.getApplicationDirectory());
        LicenceInfo info = repo.loadLicenceInfo();
        
        double hoursUsed = info.getHoursUsed() + 0.25;        
        info.setHoursUsed(hoursUsed);
        repo.saveLicenceInfo(info);
        
        if (info.isTrialExpired()) {
            // Trial period expired.            
            JOptionPane.showConfirmDialog(null, 
                    "Sorry. Your trial has been expired", 
                    "Trial Expired!", 
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
    }
}
