package main.java.no.hiof.studytracker.service;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

public class SessionService {
    private UserDataRepository userDataRepository;

    public SessionService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public boolean validateSessionData(Context ctx) {
        SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
        if (sessionDataDTO.getHours() < 0) {
            return false;
        }
        return true;
    }

    public void createStudySession(Context ctx) {
        if (validateSessionData(ctx)) {
            SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
            String token = sessionDataDTO.getToken();
            int userId = userDataRepository.getIdByTokenId(token);

            Session session = new Session(userId, sessionDataDTO.getDate(), sessionDataDTO.getHours(), sessionDataDTO.getProductivityScore(),
                    sessionDataDTO.getComment(), sessionDataDTO.getCreatedAt());
            userDataRepository.registerStudySession(session);
        }
    }
}
