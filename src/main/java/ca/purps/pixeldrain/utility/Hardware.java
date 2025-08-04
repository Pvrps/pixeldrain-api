package ca.purps.pixeldrain.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

import ca.purps.pixeldrain.exception.AppException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Hardware implements SerialNumber {

    protected String executeCommand(final String[] command, final Predicate<String> filter) throws AppException {
        try {
            final Process process = new ProcessBuilder(command).start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                final String result = br
                        .lines()
                        .filter(filter)
                        .findFirst()
                        .orElse(null);

                Hardware.log.debug("SerialNumber::executeCommand Response: " + result);

                return result;
            }
        } catch (final IOException e) {
            throw new AppException(e);
        }
    }

}
