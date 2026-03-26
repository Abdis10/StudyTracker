package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.*;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.repository.UserDataRepository;

import java.sql.Timestamp;
import java.util.List;

public class DashboardService {
    private UserDataRepository userDataRepository;
    private SessionService sessionService;

    public DashboardService(UserDataRepository userDataRepository, SessionService sessionService) {
        this.userDataRepository = userDataRepository;
        this.sessionService = sessionService;
    }

    public DashboardDTO getDashboardData(String token) {
        if (sessionService.validateToken(token)) {
            Timestamp expiresAtTimestamp = userDataRepository.getSessionTokenIdExpiresAt(token);

            if (expiresAtTimestamp != null && expiresAtTimestamp.after(new Timestamp(System.currentTimeMillis())) ) {

                int userId = userDataRepository.getUserIdByToken(token);
                DashboardDTO dashboardDTO = new DashboardDTO();
                StudySummaryDTO studySummaryDTO = new StudySummaryDTO();
                studySummaryDTO.setTodayStudyTime(userDataRepository.getTodaysStudyHours(userId));
                studySummaryDTO.setWeekStudyTime(userDataRepository.getWeekStudyHours(userId));
                studySummaryDTO.setMonthStudyTime(userDataRepository.getMonthStudyHours(userId));

                RecentStudySessionsDTO recentStudySessionsDTO = new RecentStudySessionsDTO();
                recentStudySessionsDTO.setSessions(userDataRepository.getSessions(userId));

                WeeklyProgressDTO weeklyProgressDTO = new WeeklyProgressDTO();
                weeklyProgressDTO.setThisWeekStudyHours(studySummaryDTO.getWeekStudyTime());
                weeklyProgressDTO.setLasWeekStudyHours(userDataRepository.getLastWeekStudyHours(userId));

                //List<AnalyticsDTO> analyticsDTOList = userDataRepository.analyticsData(userId);
                weeklyProgressDTO.setAnalyticsDTOList(userDataRepository.analyticsData(userId));

                dashboardDTO.setStudySummaryDTO(studySummaryDTO);
                dashboardDTO.setRecentStudySessionsDTO(recentStudySessionsDTO);
                dashboardDTO.setWeeklyProgressDTO(weeklyProgressDTO);

                return dashboardDTO;
            }
        }

        throw new CustomException("Token is not valid", "INVALID_TOKEN");
    }
}
