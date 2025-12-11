package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.model.Errortype;

public class SignupResult {
    private boolean success;
    private Errortype errortype;
    private String message;

    public SignupResult(boolean success, Errortype errortype, String message) {
        this.success = success;
        this.errortype = errortype;
        this.message = message;
    }

    public SignupResult() {}

    public boolean isSuccess() {
        return success;
    }

    public Errortype getErrorType() {
        return errortype;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorType(Errortype errorType) {
        this.errortype = errorType;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
