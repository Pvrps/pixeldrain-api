package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.utility.Hardware;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Hardware4Mac extends Hardware {

    @Override
    public String getSerialNumber() throws AppException {
        String[] command = {"/usr/sbin/system_profiler", "SPHardwareDataType"};
        String output = executeCommand(command, line -> line.contains("Serial Number"));

        if (output != null) {
            String[] splitOutput = output.split(":");
            if (splitOutput.length > 1) {
                String serialNumber = splitOutput[1].trim();
                log.debug("Hardware4Mac::getSerialNumber Response: " + serialNumber);
                return serialNumber;
            }
        }

        return null;
    }

}
