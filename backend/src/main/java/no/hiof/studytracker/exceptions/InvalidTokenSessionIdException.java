package main.java.no.hiof.studytracker.exceptions;

public class InvalidTokenSessionIdException extends RuntimeException {
    private final String errorCode;
    private int sessionId;

    public InvalidTokenSessionIdException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getSessionId() {
        return sessionId;
    }

}
