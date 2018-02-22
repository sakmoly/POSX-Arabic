package com.tecnooc.posx.licence.key;

/**
 * Created by farhan on 17/8/14.
 */
public interface SystemInfo {

    /**
     * To get MAC address
     *
     * @return String
     * @throws com.tecnooc.posx.key.SystemInfoNotFoundException
     */
    public String getMacAddress() throws SystemInfoNotFoundException;

    /**
     * To get serial id for the Hard Disk
     *
     * @return String
     * @throws com.tecnooc.posx.key.SystemInfoNotFoundException
     */
    public String getHddVolumeSerial() throws SystemInfoNotFoundException;

    /**
     * To get any CPU related Id
     *
     * @return String
     * @throws com.tecnooc.posx.key.SystemInfoNotFoundException
     */
    public String getCpuId() throws SystemInfoNotFoundException;

}
