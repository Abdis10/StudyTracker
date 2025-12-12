package main.java.no.hiof.studytracker.model;

public class SessionToken {
    private String sessionTokenId;
    private int userId;
    private String createdAt;
    private String expiresAt;

    public SessionToken(String sessionTokenId, int userId, String createdAt, String expiresAt) {
        this.sessionTokenId = sessionTokenId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getSessionTokenId() {
        return sessionTokenId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}
