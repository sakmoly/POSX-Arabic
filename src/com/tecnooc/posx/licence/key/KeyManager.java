package com.tecnooc.posx.licence.key;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Created by Farhan Ali on 8/17/2014.
 */
public class KeyManager {

    private static final String DEFAULT_KEY_1 = "ImprimerechsAWsQ";
    private static final String DEFAULT_KEY_2 = "Impr1mer@2oi2PjY";

    AesEncryption encryption = new AesEncryption();

    public String generateSystemCode() throws SystemInfoNotFoundException, GeneralSecurityException {
        SystemInfo info = new GenericSystemInfo();
        String systemInfoString = info.getCpuId() + ":"
                + info.getHddVolumeSerial() + ":" + "ImpCodeGen";

        byte[] systemCode = encryption.encrypt(DEFAULT_KEY_1, systemInfoString);
        return Base64.encode(systemCode);
    }

    public String generateProductKey(String systemCode) throws GeneralSecurityException {
        byte[] productKey = encryption.encrypt(DEFAULT_KEY_2, systemCode);
        return Base64.encode(productKey);
    }

    public boolean isValidKey(String productKey) throws GeneralSecurityException, SystemInfoNotFoundException {
        String systemCode = generateSystemCode();
        String expectedProductKey = generateProductKey(systemCode);
        return expectedProductKey.equals(productKey);
    }

}