package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.DashboardDTO;
import no.hiof.studytracker.DTOs.RecentStudySessionsDTO;
import no.hiof.studytracker.DTOs.StudySummaryDTO;
import no.hiof.studytracker.DTOs.WeeklyProgressDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.repository.UserDataRepository;

import java.sql.Timestamp;

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
                studySummaryDTO.setMonthStudyTime(userDataRepository.getMonthStudyHours(2));

                RecentStudySessionsDTO recentStudySessionsDTO = new RecentStudySessionsDTO();
                recentStudySessionsDTO.setSessions(userDataRepository.getSessions(userId));

                WeeklyProgressDTO weeklyProgressDTO = new WeeklyProgressDTO();
                weeklyProgressDTO.setThisWeekStudyHours(studySummaryDTO.getWeekStudyTime());
                weeklyProgressDTO.setLasWeekStudyHours(userDataRepository.getLastWeekStudyHours(userId));

                dashboardDTO.setStudySummaryDTO(studySummaryDTO);
                dashboardDTO.setRecentStudySessionsDTO(recentStudySessionsDTO);
                dashboardDTO.setWeeklyProgressDTO(weeklyProgressDTO);

                return dashboardDTO;
            }
        }

        throw new CustomException("Token is not valid", "INVALID_TOKEN");
    }
}
