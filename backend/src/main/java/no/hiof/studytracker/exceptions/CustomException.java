package no.hiof.studytracker.exceptions;

import java.util.List;
import java.util.Map;

public class CustomException extends RuntimeException {

    // Valgfritt: eget felt for domenedata (f.eks. username, id, osv.)
    private String errorCode;
    private List<Map<String, String>> errors;

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

    public CustomException(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}
