package no.hiof.studytracker.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException() {
        super("An account with this email may already exist.");
    }

}
