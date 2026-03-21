package no.hiof.studytracker.DTOs;

public class WeeklyProgressDTO {
    private float thisWeekStudyHours;
    private float lasWeekStudyHours;

    public WeeklyProgressDTO() {}

    public float getThisWeekStudyHours() {
        return thisWeekStudyHours;
    }

    public void setThisWeekStudyHours(float thisWeekStudyHours) {
        this.thisWeekStudyHours = thisWeekStudyHours;
    }

    public float getLasWeekStudyHours() {
        return lasWeekStudyHours;
    }

    public void setLasWeekStudyHours(float lasWeekStudyHours) {
        this.lasWeekStudyHours = lasWeekStudyHours;
    }

    @Override
    public String toString() {
        return "WeeklyProgressDTO{" +
                "thisWeekStudyHours=" + thisWeekStudyHours +
                ", lasWeekStudyHours=" + lasWeekStudyHours +
                '}';
    }
}
