package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.utility.Hardware;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Hardware4Linux extends Hardware {

    @Override
    public String getSerialNumber() throws AppException {
        String serialNumber = getDmiDecode();

        if (serialNumber == null) {
            serialNumber = getLshal();
        }

        return serialNumber;
    }

    private String getDmiDecode() throws AppException {
        String[] command = {"dmidecode", "-t", "system"};
        String output = executeCommand(command, line -> line.contains("Serial Number:"));

        if (output != null) {
            String[] splitOutput = output.split("Serial Number:");
            if (splitOutput.length > 1) {
                String serialNumber = splitOutput[1].trim();
                log.debug("Hardware4Linux::getSerialNumber[DMI] Response: " + serialNumber);
                return serialNumber;
            }
        }

        return null;
    }

    private String getLshal() throws AppException {
        String[] command = {"lshal"};
        String output = executeCommand(command, line -> line.contains("system.hardware.serial"));

        if (output != null) {
            String[] splitOutput = output.split("=");
            if (splitOutput.length > 1) {
                String serialNumber = splitOutput[1].replaceAll("\\(string\\)|(')", "").trim();
                log.debug("Hardware4Linux::getSerialNumber[LSHAL] Response: " + serialNumber);
                return serialNumber;
            }
        }

        return null;
    }

}