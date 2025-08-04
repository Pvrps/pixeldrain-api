package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.utility.Hardware;
import lombok.extern.log4j.Log4j2;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;

@Log4j2
public class DefaultHardware extends Hardware {

    @Override
    public String getSerialNumber() throws AppException {
        try {
            final SystemInfo systemInfo = new SystemInfo();
            final ComputerSystem computerSystem = systemInfo.getHardware().getComputerSystem();
            final String serialNumber = computerSystem.getSerialNumber();
            DefaultHardware.log.debug("Hardware4Mac::getSerialNumber Response: " + serialNumber);
            return serialNumber;
        } catch (final Exception e) {
            throw new AppException(e);
        }
    }
    
}
