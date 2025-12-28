package main.java.no.hiof.studytracker.exceptions;

public class CustomException extends RuntimeException {

    // Valgfritt: eget felt for domenedata (f.eks. username, id, osv.)
    private final String errorCode;

    // 1. Standard melding
    public CustomException(String message) {
        super(message);
        this.errorCode = null;
    }

    // 2. Melding + årsak (nested exception)
    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    // 3. Kun årsak
    public CustomException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    // 4. Melding + errorCode (vanlig i API-feil)
    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // 5. Hente errorCode hvis du bruker det
    public String getErrorCode() {
        return errorCode;
    }
}
