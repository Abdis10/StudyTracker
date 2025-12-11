package main.java.no.hiof.studytracker.exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super("Invalid password: " + message);
    }
}
