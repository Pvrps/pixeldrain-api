package ca.purps.pixeldrain.utility;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.utility.impl.Hardware4Linux;
import ca.purps.pixeldrain.utility.impl.Hardware4Mac;
import ca.purps.pixeldrain.utility.impl.Hardware4Windows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.SystemUtils;

import java.util.Base64;

@Log4j2
public final class HardwareFactory {

    private HardwareFactory() {}

    public static String getSerialNumber() throws AppException {
        Hardware hardware;

        if (SystemUtils.IS_OS_WINDOWS) {
            hardware = new Hardware4Windows();
        } else if (SystemUtils.IS_OS_LINUX) {
            hardware = new Hardware4Linux();
        } else if (SystemUtils.IS_OS_MAC) {
            hardware = new Hardware4Mac();
        } else {
            throw new AppException("Your OS could not be identified.");
        }

        String serialNumber = hardware.getSerialNumber();

        if (serialNumber == null) {
            serialNumber = getDefaultSerialNumber();
        }

        return serialNumber;
    }

    private static String getDefaultSerialNumber() {
        String result = SystemUtils.getHostName() + SystemUtils.getUserDir() + SystemUtils.getJavaHome();
        String serialNumber = Base64.getEncoder().encodeToString(result.getBytes());

        log.debug("HardwareFactory::getSerialNumber[DEFAULT] Response: " + serialNumber);

        return serialNumber;
    }

}
