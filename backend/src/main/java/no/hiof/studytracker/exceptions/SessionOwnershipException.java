package main.java.no.hiof.studytracker.exceptions;

public class SessionOwnershipException extends RuntimeException {
    private final String errorCode;
    private int sessionId;

    public SessionOwnershipException(String message, String errorCode) {
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
