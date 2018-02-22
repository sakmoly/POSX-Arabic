package com.tecnooc.posx.licence.key;

/**
 * Created by farhan on 17/8/14.
 */
public class LinuxSystemInfo implements SystemInfo {

    @Override
    public String getMacAddress() throws SystemInfoNotFoundException {
        String command = "/sbin/ifconfig";
        String pattern = "([a-fA-F0-9]{1,2}(-|:)){5}[a-fA-F0-9]{1,2}";
        String mac = null;

        try {
            CommandExecuter executer = new CommandExecuter();
            mac = executer.executeAndFindPattern(command, pattern);

            if(mac == null){
                throw new SystemInfoNotFoundException();
            }
        } catch (Exception e) {
            throw new SystemInfoNotFoundException("MAC address cannot find, command '" + command + "' may not be " +
                    "supported by the Operating System or Required data is not found on the output", e);
        }

        return mac;
    }

    @Override
    public String getHddVolumeSerial() throws SystemInfoNotFoundException {
        String command = "/sbin/udevadm info --query=property --name=sda";
        String key = "ID_SERIAL_SHORT";
        String hddSerial = null;

        try {
            CommandExecuter executer = new CommandExecuter();
            hddSerial = executer.executeAndFindLineWithKey(command, key);
            hddSerial = hddSerial.split("=")[1];
        } catch (Exception e) {
            throw new SystemInfoNotFoundException("HDD Serial cannot find, command '" + command + "' may not be " +
                    "supported by the Operating System or Required data is not found on the output", e);
        }

        return hddSerial;
    }

    @Override
    public String getCpuId() throws SystemInfoNotFoundException {
        String command = "lscpu";
        String keyArchitecture = "Architecture";
        String keyFamily = "CPU family";
        String keyModel = "Model";
        String keyStepping = "Stepping";
        String keyVendorId = "Vendor ID";
        String cpuId = null;

        try {
            CommandExecuter executer = new CommandExecuter();

            String architecture = executer.executeAndFindLineWithKey(command, keyArchitecture) + " ";
            String family = executer.executeAndFindLineWithKey(command, keyFamily) + " ";
            String model = executer.executeAndFindLineWithKey(command, keyModel) + " ";
            String stepping = executer.executeAndFindLineWithKey(command, keyStepping) + ", ";
            String vendorId = executer.executeAndFindLineWithKey(command, keyVendorId);

            cpuId = architecture + family + model + stepping +vendorId;
            //to make pattern same as in windows
            cpuId = cpuId.replaceAll(" +", " ").replaceAll(": ", " ").replace("Architecture ", "")
                    .replace("CPU f", "F").replace("Vendor ID ", "");

        } catch (Exception e) {
            throw new SystemInfoNotFoundException("CPUID cannot find, command '" + command + "' may not be " +
                    "supported by the Operating System or Required data is not found on the output", e);
        }

        return cpuId;
    }

}
