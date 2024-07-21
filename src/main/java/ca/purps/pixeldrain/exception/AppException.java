package ca.purps.pixeldrain.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppException extends Exception {

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
        log.error(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

    public AppException(Throwable cause) {
        super(cause);
        log.error(cause.getMessage());
    }

}
