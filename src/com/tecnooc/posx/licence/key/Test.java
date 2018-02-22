package com.tecnooc.posx.licence.key;

import java.security.GeneralSecurityException;

/**
 * Created by farhan on 17/8/14.
 */
public class Test {
    //test SystemInfo
   /* public static void main(String[] args) {

        String os = System.getProperty("os.name");
        System.out.println("Operating System : " + os);

        SystemInfo si = new GenericSystemInfo();

        try {
            System.out.println("MAC : " + si.getMacAddress());
            System.out.println("HDD Serial : " + si.getHddVolumeSerial());
            System.out.printf("CPUID : " + si.getCpuId());
        } catch (SystemInfoNotFoundException e) {
            e.printStackTrace();
        }
    }*/


    //test KeyManager
    public static void main(String[] args) {
        KeyManager keyManager = new KeyManager();
        String systemCode = null;
        try {
            systemCode = keyManager.generateSystemCode();
            System.out.println("System Code : " + systemCode);
            String prodKey = keyManager.generateProductKey(systemCode);
            //prodKey += "hel";
            System.out.println("Product Key : " + prodKey);
            System.out.println("Is Valid : " + keyManager.isValidKey(prodKey));
        } catch (SystemInfoNotFoundException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        try {
            String key = "ThisIsASecretKey";
            byte[] ciphertext = encrypt(key, "alifarhan is a hero..");
            System.out.println("decrypted value:" + (decrypt(key, ciphertext)));

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }*/

}
