package no.hiof.studytracker.DTOs;

import no.hiof.studytracker.model.Session;

public class DashboardDTO {
    private StudySummaryDTO studySummaryDTO;
    private RecentStudySessionsDTO recentStudySessionsDTO;
    private WeeklyProgressDTO weeklyProgressDTO;

    public DashboardDTO() {}

    public StudySummaryDTO getStudySummaryDTO() {
        return studySummaryDTO;
    }

    public void setStudySummaryDTO(StudySummaryDTO studySummaryDTO) {
        this.studySummaryDTO = studySummaryDTO;
    }

    public RecentStudySessionsDTO getRecentStudySessionsDTO() {
        return recentStudySessionsDTO;
    }

    public void setRecentStudySessionsDTO(RecentStudySessionsDTO recentStudySessionsDTO) {
        this.recentStudySessionsDTO = recentStudySessionsDTO;
    }

    public WeeklyProgressDTO getWeeklyProgressDTO() {
        return weeklyProgressDTO;
    }

    public void setWeeklyProgressDTO(WeeklyProgressDTO weeklyProgressDTO) {
        this.weeklyProgressDTO = weeklyProgressDTO;
    }
}
