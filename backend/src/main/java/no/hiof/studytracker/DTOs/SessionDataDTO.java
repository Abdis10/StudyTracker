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

    public SessionDataDTO() {}

    public SessionDataDTO(String date, float hours, int productivityScore, String comment, Timestamp createdAt) {
        this.date = date;
        this.hours = hours;
        this.productivityScore = productivityScore;
        this.comment = comment;
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "{" + '\n' +
                "date: " + date + '\n' +
                "hours: " + hours + '\n' +
                "productivityScore: " + productivityScore + '\n' +
                "comment: " + comment + '\n' +
                "created at: " + createdAt + '\n' +
                "updated_at: " + updatedAt + '\n' +
                '}';
    }



}
