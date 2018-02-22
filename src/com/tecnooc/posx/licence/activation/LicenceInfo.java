/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.activation;

import java.io.Serializable;

/**
 *
 * @author jomit
 */
public class LicenceInfo implements Serializable {
    private boolean activated;
    private double hoursUsed;
    private String productKey;

    public LicenceInfo() {
    }

    public LicenceInfo(boolean activated, double hoursUsed, String productKey) {
        this.activated = activated;
        this.hoursUsed = hoursUsed;
        this.productKey = productKey;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public double getHoursUsed() {
        return hoursUsed;
    }

    public void setHoursUsed(double hoursUsed) {
        this.hoursUsed = hoursUsed;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }
    
    public boolean isTrialExpired() {
        if (activated) 
            return false;
        
        return hoursUsed > 240;
    }
    
    public double getHoursRemaining() {
        if (isTrialExpired())
            return 0;
        
        return 240 - hoursUsed;
    }

    @Override
    public String toString() {
        String info = 
                "activated : " + activated + "\n" + 
                "hoursUsed : " + hoursUsed + "\n" + 
                "productKey: " + productKey + "\n";
        return info;
    }
}
