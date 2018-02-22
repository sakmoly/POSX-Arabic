/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author jomit
 */
public class Util {
    public static void exitWithErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, "<html><body width='400'><p>Internal Error.<br><br>" + message, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    
    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, "<html><body width='400'><p>Internal Error.<br><br>" + message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showInternalExceptionMessage(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "<html><body width='400'><p>Internal Error.<br><br>" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static Long generateSid() {        
        int sidRandPart = random.nextInt(SID_RAND_LIMIT);
        if (sidRandPart < SID_RAND_START) {
            sidRandPart += SID_RAND_START;
        }
        String sid = new Date().getTime() + "" + sidRandPart;
        return Long.parseLong(sid);
    }
       
    private static final Random random = new Random();
    private static final int SID_RAND_LIMIT = 10000;
    private static final int SID_RAND_START = 1000;
}
