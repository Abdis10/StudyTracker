package main.java.no.hiof.studytracker.exceptions;

public class StudySessionException extends RuntimeException{
    private float hours;
    private String token;
    private String message;

    public StudySessionException(String message) {
        super(message);
        this.hours = 0;
        this.token = null;
    }

    public float getHours() {
        return hours;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
