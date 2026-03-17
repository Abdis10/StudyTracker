package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.DashboardDTO;
import no.hiof.studytracker.DTOs.StudySummaryDTO;
import no.hiof.studytracker.repository.UserDataRepository;

public class DashboardService {
    private UserDataRepository userDataRepository;
    private SessionService sessionService;

    public DashboardService(UserDataRepository userDataRepository, SessionService sessionService) {
        this.userDataRepository = userDataRepository;
        this.sessionService = sessionService;
    }

    public void getDashboardData(String token) {
        if (sessionService.validateToken(token)) {
            int userId = userDataRepository.getUserIdByToken(token);
            DashboardDTO dashboardDTO = new DashboardDTO();
            StudySummaryDTO studySummaryDTO = new StudySummaryDTO();
            studySummaryDTO.setTodayStudyTime(userDataRepository.getTodaysStudyHours(userId));

        }
    }
}
