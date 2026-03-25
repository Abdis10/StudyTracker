package no.hiof.studytracker.DTOs;

import java.util.ArrayList;
import java.util.List;

public class WeeklyProgressDTO {
    private float thisWeekStudyHours;
    private float lasWeekStudyHours;
    private List<AnalyticsDTO> analyticsDTOList;

    public WeeklyProgressDTO() {
        analyticsDTOList = new ArrayList<>();
    }

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

    public List<AnalyticsDTO> getAnalyticsDTOList() {
        return analyticsDTOList;
    }

    public void setAnalyticsDTOList(List<AnalyticsDTO> analyticsDTOList) {
        this.analyticsDTOList = analyticsDTOList;
    }

    @Override
    public String toString() {
        return "WeeklyProgressDTO{" +
                "thisWeekStudyHours=" + thisWeekStudyHours +
                ", lasWeekStudyHours=" + lasWeekStudyHours +
                ", analyticsDTOList=" + analyticsDTOList +
                '}';
    }
}
