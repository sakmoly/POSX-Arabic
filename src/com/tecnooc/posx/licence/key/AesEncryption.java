package com.tecnooc.posx.licence.key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

public class AesEncryption {

    public byte[] encrypt(String key, String str) throws GeneralSecurityException {
        return crypto(Cipher.ENCRYPT_MODE, key, str);
    }

    public byte[] decrypt(String key, String str) throws GeneralSecurityException {
        return crypto(Cipher.DECRYPT_MODE, key, str);
    }    
    
    protected byte[] crypto(int mode, String key, String str) throws GeneralSecurityException {
        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, skeySpec,
                new IvParameterSpec(new byte[16]));
        byte[] data = cipher.doFinal(str.getBytes(Charset.forName("UTF-8")));

        return data;
    }
}