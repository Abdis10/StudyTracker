package main.java.no.hiof.studytracker.exceptions;

public class DatabaseException extends RuntimeException {
    private final String errorCode;

    public DatabaseException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    public DatabaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DatabaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
