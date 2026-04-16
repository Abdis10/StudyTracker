package no.hiof.studytracker.DTOs;

import java.sql.Timestamp;

public class SessionDataDTO {
    private String token;
    private int userId;
    private String date;
    private float hours;
    private int productivityScore;
    private String comment;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer subjectId;

    public SessionDataDTO() {}

    public SessionDataDTO(String token, String date, float hours, int productivityScore,
                          String comment, Timestamp createdAt, Integer subjectId) {
        this.token = token;
        this.date = date;
        this.hours = hours;
        this.productivityScore = productivityScore;
        this.comment = comment;
        this.createdAt = createdAt;
        this.subjectId = subjectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public int getProductivityScore() {
        return productivityScore;
    }

    public void setProductivityScore(int productivityScore) {
        this.productivityScore = productivityScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "SessionDataDTO{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", hours=" + hours +
                ", productivityScore=" + productivityScore +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", subjectId=" + subjectId +
                '}';
    }
}
