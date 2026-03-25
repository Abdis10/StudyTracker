package no.hiof.studytracker.DTOs;

public class AnalyticsDTO {
    private String date;
    private float hours;
    private int productivityScore;

    public AnalyticsDTO() {}

    public AnalyticsDTO(String date, float hours, int productivityScore) {
        this.date = date;
        this.hours = hours;
        this.productivityScore = productivityScore;
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

    @Override
    public String toString() {
        return "AnalyticsDTO{" +
                "date='" + date + '\'' +
                ", hours=" + hours +
                ", productivityScore=" + productivityScore +
                '}';
    }
}
