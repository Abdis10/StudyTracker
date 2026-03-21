package no.hiof.studytracker.DTOs;

public class StudySummaryDTO {
    private float todayStudyTime;
    private float weekStudyTime;
    private float monthStudyTime;

    public StudySummaryDTO() {}

    public float getTodayStudyTime() {
        return todayStudyTime;
    }

    public void setTodayStudyTime(float todayStudyTime) {
        this.todayStudyTime = todayStudyTime;
    }

    public float getWeekStudyTime() {
        return weekStudyTime;
    }

    public void setWeekStudyTime(float weekStudyTime) {
        this.weekStudyTime = weekStudyTime;
    }

    public float getMonthStudyTime() {
        return monthStudyTime;
    }

    public void setMonthStudyTime(float monthStudyTime) {
        this.monthStudyTime = monthStudyTime;
    }

    @Override
    public String toString() {
        return "StudySummaryDTO{" +
                "todayStudyTime=" + todayStudyTime +
                ", weekStudyTime=" + weekStudyTime +
                ", monthStudyTime=" + monthStudyTime +
                '}';
    }
}
