package ca.purps.pixeldrain.utility;

import java.util.Base64;

import org.apache.commons.lang3.SystemUtils;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.utility.impl.DefaultHardware;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class HardwareFactory {

    private HardwareFactory() {}

    public static String getSerialNumber() throws AppException {
        final Hardware hardware = new DefaultHardware();

        String serialNumber = hardware.getSerialNumber();

        if (serialNumber == null) {
            serialNumber = HardwareFactory.getDefaultSerialNumber();
        }

        return serialNumber;
    }

    private static String getDefaultSerialNumber() {
        final String result = SystemUtils.getHostName() + SystemUtils.getUserDir() + SystemUtils.getJavaHome();
        final String serialNumber = Base64.getEncoder().encodeToString(result.getBytes());

        HardwareFactory.log.debug("HardwareFactory::getSerialNumber[DEFAULT] Response: " + serialNumber);

        return serialNumber;
    }

}
