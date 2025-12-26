package main.java.no.hiof.studytracker.service;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.DTOs.SessionResponseDTO;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

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
            int userId = userDataRepository.getUserIdByToken(token);

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
        int userId = userDataRepository.getUserIdByToken(token);
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


    /*
        1. Autentiser token
        2. Hvis token er gyldig sjekk om den tilhører brukeren som ber om oppdateringen
        3. Oppdater kun de feltene som brukeren ber endring om (la stå gamle verdier hvis de ikke er med i forespørselen)
    */


    public boolean doesTokenMatchUser(String token, int sessionId) {
        int userIdByToken = userDataRepository.getUserIdByToken(token);
        int userIdBySessionId = userDataRepository.getUserIdBySessionId(sessionId);
        if (validateToken(token) && (userIdByToken == userIdBySessionId)) {
            return true;
        }
        return false;
    }

    public boolean updateSessionInRepo(SessionDataDTO sessionDataDTO, String token, int sessionId) {
        SessionDataDTO sessionDataDTO1 = new SessionDataDTO();
        if (doesTokenMatchUser(token, sessionId)) {
            String updatedAt = LocalDateTime.now().toString();
            sessionDataDTO1 = sessionDataDTO;
            sessionDataDTO1.setUpdatedAt(updatedAt);

            if (isEmptyOrNullOrZero(sessionDataDTO.getDate())) {
                sessionDataDTO1.setDate(userDataRepository.getSessionBySessionId(sessionId).getDate());
            }
            if (isEmptyOrNullOrZero(sessionDataDTO.getHours())) {
                sessionDataDTO1.setHours(userDataRepository.getSessionBySessionId(sessionId).getHours());
            }
            if (isEmptyOrNullOrZero(sessionDataDTO.getProductivityScore())) {
                sessionDataDTO1.setProductivityScore(userDataRepository.getSessionBySessionId(sessionId).getProductivityScore());
            }
            if (isEmptyOrNullOrZero(sessionDataDTO.getComment())) {
                sessionDataDTO1.setComment(userDataRepository.getSessionBySessionId(sessionId).getComment());
            }

            sessionDataDTO1.setCreatedAt(userDataRepository.getSessionBySessionId(sessionId).getCreatedAt());
            if (userDataRepository.updateSession(sessionId, sessionDataDTO1) == 1) {
                return true;
            }
        }

        throw new CustomException("Session does not exist", "NON_EXISTENT_SESSION");
    }

    public boolean isEmptyOrNullOrZero(Object o) {
        if (o.equals("") || o.equals(null) || o.equals(0) || o.toString().equals("0.0") ) {
            return true;
        }
        return false;
    }

}
