package no.hiof.studytracker.DTOs;

public class WeeklyProgressDTO {
    private int thisWeekStudyHours;
    private int lasWeekStudyHours;

    public WeeklyProgressDTO() {}

    public int getThisWeekStudyHours() {
        return thisWeekStudyHours;
    }

    public void setThisWeekStudyHours(int thisWeekStudyHours) {
        this.thisWeekStudyHours = thisWeekStudyHours;
    }

    public int getLasWeekStudyHours() {
        return lasWeekStudyHours;
    }

    public void setLasWeekStudyHours(int lasWeekStudyHours) {
        this.lasWeekStudyHours = lasWeekStudyHours;
    }
}
