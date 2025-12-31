package no.hiof.studytracker.DTOs;

public class SessionDataDTO {
    private String token;
    private int userId;
    private String date;
    private float hours;
    private int productivityScore;
    private String comment;
    private String createdAt;
    private String updatedAt;

    public SessionDataDTO() {}

    public SessionDataDTO(String date, float hours, int productivityScore, String comment, String createdAt) {
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
