package no.hiof.studytracker.exceptions;

public class UserAuthenticationException extends RuntimeException {

    public UserAuthenticationException() {
        super("User authentication failed");
    }

}
