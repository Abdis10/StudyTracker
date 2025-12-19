package main.java.no.hiof.studytracker.model;

public class Session {
    private int id;
    private int userId;
    private String date;
    private float hours;
    private int productivityScore;
    private String comment;
    private String createdAt;
    private String updatedAt;

    public Session(int id, int userId, String date, float hours, int productivityScore, String comment, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
        this.productivityScore = productivityScore;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Session(int userId, String date, float hours, int productivityScore, String comment, String createdAt) {
        this.userId = userId;
        this.date = date;
        this.hours = hours;
        this.productivityScore = productivityScore;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", hours=" + hours +
                ", productivityScore=" + productivityScore +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
