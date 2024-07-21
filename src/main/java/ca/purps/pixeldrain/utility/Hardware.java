package ca.purps.pixeldrain.utility;

import ca.purps.pixeldrain.exception.AppException;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

@Log4j2
public abstract class Hardware implements SerialNumber {

    protected String executeCommand(String[] command, Predicate<String> filter) throws AppException {
        try {
            Process process = new ProcessBuilder(command).start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String result = br
                        .lines()
                        .filter(filter)
                        .findFirst()
                        .orElse(null);

                log.debug("SerialNumber::executeCommand Response: " + result);

                return result;
            }
        } catch (IOException e) {
            throw new AppException(e);
        }
    }

}
