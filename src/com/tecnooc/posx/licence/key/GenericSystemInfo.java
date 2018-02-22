package com.tecnooc.posx.licence.key;

/**
 * Created by Farhan Ali on 8/17/2014.
 */
public class GenericSystemInfo implements SystemInfo {

    SystemInfo info = null;

    public GenericSystemInfo() {
        String os = System.getProperty("os.name");

        if(os.contains("Linux")){
            info = new LinuxSystemInfo();
        } else if(os.contains("Windows")) {
            info = new WindowsSystemInfo();
        }
    }

    @Override
    public String getMacAddress() throws SystemInfoNotFoundException {
        return info.getMacAddress();
    }

    @Override
    public String getHddVolumeSerial() throws SystemInfoNotFoundException {
        return info.getHddVolumeSerial();
    }

    @Override
    public String getCpuId() throws SystemInfoNotFoundException {
        return info.getCpuId();
    }

}
