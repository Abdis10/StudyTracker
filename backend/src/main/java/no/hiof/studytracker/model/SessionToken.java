package no.hiof.studytracker.model;

import java.sql.Time;
import java.sql.Timestamp;

public class SessionToken {
    private String sessionTokenId;
    private int userId;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public SessionToken(String sessionTokenId, int userId, Timestamp createdAt, Timestamp expiresAt) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }
}
