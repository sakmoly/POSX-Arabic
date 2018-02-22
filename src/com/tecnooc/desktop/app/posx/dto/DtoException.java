/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.dto;

import java.math.BigDecimal;

/**
 *
 * @author jomit
 */
public class DtoException extends Exception {
    private BigDecimal maxDiscPerc;

    public DtoException(String message) {
        super(message);
    }

    public DtoException(String message, Throwable cause) {
        super(message, cause);
    }  
    
    public DtoException(String message, BigDecimal maxDiscPerc) {
        super(message);
        
        this.maxDiscPerc = maxDiscPerc;
    }  

    public BigDecimal getMaxDiscPerc() {
        return maxDiscPerc;
    }
}
