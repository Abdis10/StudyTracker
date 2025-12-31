package no.hiof.studytracker.exceptions;

public class UserAuthenticationException extends RuntimeException{
    private String email;

    public UserAuthenticationException(String email) {
        super("Invalid authentication of user with: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
