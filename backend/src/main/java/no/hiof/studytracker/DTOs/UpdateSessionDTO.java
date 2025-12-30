package no.hiof.studytracker.DTOs;

public class UpdateSessionDTO {
    private String token;
    private int userId;
    private String date;
    private Float hours;
    private Integer productivityScore;
    private String comment;
    private String createdAt;
    private String updatedAt;

    public UpdateSessionDTO() {}

    public UpdateSessionDTO(String date, Float hours, Integer productivityScore, String comment, String createdAt) {
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

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    public Integer getProductivityScore() {
        return productivityScore;
    }

    public void setProductivityScore(Integer productivityScore) {
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
