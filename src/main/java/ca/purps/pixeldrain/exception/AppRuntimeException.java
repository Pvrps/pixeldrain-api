package ca.purps.pixeldrain.exception;

public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException() {
        super();
    }

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(Throwable cause) {
        super(cause);
    }

}
