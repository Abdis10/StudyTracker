package main.java.no.hiof.studytracker.service;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.exceptions.StudySessionException;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

import java.time.LocalDateTime;

public class SessionService {
    private UserDataRepository userDataRepository;

    public SessionService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public void validateSessionData(Context ctx) {
        SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
        if (sessionDataDTO.getHours() < 0) {
            throw new StudySessionException("Number of hours must be higher than 0");
        }

        if (sessionDataDTO.getToken().equalsIgnoreCase("")); {
            throw new StudySessionException("Empty token from the request!");
        }
    }

    public void createStudySession(Context ctx) {
            SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
            String token = sessionDataDTO.getToken();
            int userId = userDataRepository.getIdByTokenId(token);

            if (userId != 0) {
                String createdAt = LocalDateTime.now().toString();
                sessionDataDTO.setCreatedAt(createdAt);

                Session session = new Session(userId, sessionDataDTO.getDate(), sessionDataDTO.getHours(), sessionDataDTO.getProductivityScore(),
                        sessionDataDTO.getComment(), sessionDataDTO.getCreatedAt());
                userDataRepository.registerStudySession(session);
            }
    }

    public void studySession(Context ctx) {
        validateSessionData(ctx);
        createStudySession(ctx);
    }
}
