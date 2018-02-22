package com.tecnooc.posx.licence.key;

import java.io.IOException;

/**
 * Created by farhan on 17/8/14.
 */
public class WindowsSystemInfo implements SystemInfo {

    @Override
    public String getMacAddress() throws SystemInfoNotFoundException {
        String command = "getmac";
        String pattern = "([a-fA-F0-9]{1,2}(-|:)){5}[a-fA-F0-9]{1,2}";
        String mac = null;

        try {
            CommandExecuter executer = new CommandExecuter();
            mac = executer.executeAndFindPattern(command, pattern);

            if(mac == null){
            throw new SystemInfoNotFoundException("MAC address cannot find, Required data is empty");
        }
    } catch (IOException e) {
        throw new SystemInfoNotFoundException("MAC address cannot find, command '" + command + "' may not be " +
                "supported by the Operating System or Required data is not found on the output ", e);
    } catch (InterruptedException e) {
        throw new SystemInfoNotFoundException("MAC address cannot find, command '" + command + "' may not be " +
                "supported by the Operating System or Required data is not found on the output ", e);
    }

        return mac;
    }

    @Override
    public String getHddVolumeSerial() throws SystemInfoNotFoundException {
        String command = "wmic diskdrive get serialnumber";
        String hddSerial = null;

        try {
            CommandExecuter executer = new CommandExecuter();
            String out = executer.execute(command);
            hddSerial = out.replaceAll(" +", "").replace("SerialNumber", "");
        } catch (Exception e) {
            throw new SystemInfoNotFoundException("HDD Serial cannot find, command '" + command + "' may not be " +
                    "supported by the Operating System or Required data is not found on the output", e);
        }

        return hddSerial;
    }

    @Override
    public String getCpuId() throws SystemInfoNotFoundException {
        String command = "cmd.exe /c echo %PROCESSOR_IDENTIFIER%";
        String cpuId = null;

        CommandExecuter executer = new CommandExecuter();
        try {
            cpuId = executer.execute(command);
        } catch (Exception e) {
            throw new SystemInfoNotFoundException("CPUID cannot find, command '" + command + "' may not be " +
                    "supported by the Operating System or Required data is not found on the output", e);
        }

        return cpuId;
    }

}
