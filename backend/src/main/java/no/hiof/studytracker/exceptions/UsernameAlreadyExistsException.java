package no.hiof.studytracker.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    private final String username;

    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
