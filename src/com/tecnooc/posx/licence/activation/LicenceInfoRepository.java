/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.activation;

import com.lowagie.text.pdf.codec.Base64;
import com.tecnooc.posx.licence.file.FileHandler;
import java.io.File;
import java.io.Serializable;

/**
 *
 * @author jomit
 */
public class LicenceInfoRepository {
    private static String key = "tec-posx#2014";
    private static String licenceFileName = "licence";
    
    private File applicationDirectory;
    private File licenceFile;
    
    private FileHandler fileHandler;

    public LicenceInfoRepository(File applicationDirectory) {
        this.applicationDirectory = applicationDirectory;
        
        String licenceFilePath = applicationDirectory.getAbsolutePath() + "/" + licenceFileName;
        this.licenceFile = new File(licenceFilePath);
        
        this.fileHandler = new FileHandler();
    }
    
    public LicenceInfo loadLicenceInfo() {
        try {
            String licenceStr = fileHandler.readFileContent(licenceFile);
            LicenceInfo info  = (LicenceInfo) stringToObject(licenceStr);
            
            return info;
        } catch (Exception ex) {
            return new LicenceInfo(false, 1000, "");
        }
    }
    
    public void saveLicenceInfo(LicenceInfo info) {
        try {
            String licence    = objectToString(info);
            fileHandler.writeFileContent(licenceFile, licence);
        } catch (Exception ex) {
        }
    }
    
    /** Write the object to a Base64 string. */
    private String objectToString(Serializable obj) {
        return Base64.encodeObject(obj);
    }
    
    /** Read the object from Base64 string. */
    private Object stringToObject(String str) {
        return Base64.decodeToObject(str);
    }
    
    /*
    private String crypto(String str, int mode) throws Exception {
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(mode, key);
            byte[] output = cipher.doFinal(str.getBytes());
            return new String(output);
        } catch (Exception e) {
            throw e;
        }
    }*/
}
