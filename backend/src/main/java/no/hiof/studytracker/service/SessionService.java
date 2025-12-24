package main.java.no.hiof.studytracker.service;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.DTOs.SessionResponseDTO;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SessionService {
    private UserDataRepository userDataRepository;

    public SessionService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public void validateSessionData(Context ctx) {
        SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
        if (sessionDataDTO.getHours() < 0) {
            throw new CustomException("Number of hours must be higher than 0", "INVALID_HOURS");
        }

        String token = sessionDataDTO.getToken();
        if (!userDataRepository.doesTokenExist(token)) {
            throw new CustomException("Token couldn't be verified", "UNIDENTIFIED_TOKEN");
        }


    }

    public void createStudySession(Context ctx) {
            SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
            String token = sessionDataDTO.getToken();
            int userId = userDataRepository.getIdByToken(token);

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


    public boolean validateToken(String token) {
        if (userDataRepository.doesTokenExist(token)) {
            return true;
        }
        return false;
    }

    /*
        Vi får 2 utfall når vi prøver å hente studieøkter
        1. Success - token er gyldig og da returnere liste av studieøkter (eventuelt en tom liste)
        2. Invalid_token - ugyldig token
    */

    public List<SessionResponseDTO>  getSessionsFromRepository(String token) {
        int userId = userDataRepository.getIdByToken(token);
        List<SessionResponseDTO> listOfSessions = userDataRepository.getSessions(userId);

        List<SessionResponseDTO> sortedList = listOfSessions.stream()
                        .sorted(Comparator.comparing(SessionResponseDTO::getDate,
                                Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                        .toList();
        return sortedList;
    }

    public List<SessionResponseDTO> getSessions(String token) {
        if (validateToken(token)) {
            return getSessionsFromRepository(token);
        }

        else {
            throw new CustomException("Invalid token", "UNAUTHORIZED_TOKEN");
        }
    }

}
