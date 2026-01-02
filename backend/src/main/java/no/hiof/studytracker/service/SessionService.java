package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.SessionDataDTO;
import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.exceptions.SessionOwnershipException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.repository.UserDataRepository;

import java.time.LocalDateTime;
import java.util.*;

public class SessionService {
    private UserDataRepository userDataRepository;

    public SessionService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public void validateSessionData(SessionDataDTO sessionDataDTO) {
        if (sessionDataDTO.getHours() < 0) {
            throw new CustomException("Number of hours must be higher than 0", "INVALID_HOURS");
        }

        String token = sessionDataDTO.getToken();
        if (!userDataRepository.doesTokenExist(token)) {
            throw new CustomException("Token couldn't be verified", "UNIDENTIFIED_TOKEN");
        }
    }

    public void createStudySession(SessionDataDTO sessionDataDTO) {
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

    public void studySession(SessionDataDTO sessionDataDTO) {
        validateSessionData(sessionDataDTO);
        createStudySession(sessionDataDTO);
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


    public boolean doesTokenMatchUser(String token, int sessionId) {
        int userIdByToken = userDataRepository.getUserIdByToken(token);
        int userIdBySessionId = userDataRepository.getUserIdBySessionId(sessionId);
        if ((userIdByToken == userIdBySessionId)) {
            return true;
        }
        return false;
    }


    /**
     * Updates an existing session by merging new values with existing data.
     * Fields that are null or empty in the request are preserved from the database.
     * Validates that the given token belongs to the owner of the session.
     *
     * @param updateSessionDTO  incoming update data (partial update)
     * @param token             authentication token
     * @param sessionId         id of the session to update
     * @return true if update succeeds
     * @throws CustomException  if token is invalid or session does not belong to user
     */

    public boolean updateSession(UpdateSessionDTO updateSessionDTO, String token, int sessionId) {
        UpdateSessionDTO updateSessionDTO1 = new UpdateSessionDTO();

        // Fetch existing session data for fallback values
        UpdateSessionDTO existingSessionFromRepo = userDataRepository.getSessionBySessionId(sessionId);

        // Verify that token owner matches session owner
        if (doesTokenMatchUser(token, sessionId)) {
            // Set update timestamp
            String updatedAt = LocalDateTime.now().toString();
            updateSessionDTO1.setUpdatedAt(updatedAt);

            // Preserve existing values when fields are null or empty
            if (isEmptyOrNull(updateSessionDTO.getDate())) {
                updateSessionDTO1.setDate(existingSessionFromRepo.getDate());
            }

            else {
                updateSessionDTO1.setDate(updateSessionDTO.getDate());
            }

            if (isEmptyOrNull(updateSessionDTO.getComment())) {
                updateSessionDTO1.setComment(existingSessionFromRepo.getComment());
            }

            else {
                updateSessionDTO1.setComment(updateSessionDTO.getComment());
            }

            if (isNull(updateSessionDTO.getHours())) {
                updateSessionDTO1.setHours(existingSessionFromRepo.getHours());
            }

            else {
                updateSessionDTO1.setHours(updateSessionDTO.getHours());
            }

            if (isNull(updateSessionDTO.getProductivityScore())) {
                updateSessionDTO1.setProductivityScore(existingSessionFromRepo.getProductivityScore());
            }

            else {
                updateSessionDTO1.setProductivityScore(updateSessionDTO.getProductivityScore());
            }

            // Preserve original creation timestamp
            updateSessionDTO1.setCreatedAt(existingSessionFromRepo.getCreatedAt());

            // Execute update and verify that session exists
            int valueOfUpdateSQLUpdateQuery = userDataRepository.updateSession(sessionId, updateSessionDTO1);

            if (valueOfUpdateSQLUpdateQuery == 0) {             // No rows updated means session does not exist
                throw new CustomException("Session does not exist", "NON_EXISTENT_SESSION");
            }
            return true;
        }

        // Token does not match session owner
        throw new SessionOwnershipException("Given token and session-id doesn't match user", "INVALID_TOKEN_SESSION_ID");
    }


    public void updateSessionInRepo(UpdateSessionDTO updateSessionDTO, String token, int sessionId) {
        if (userDataRepository.doesTokenExist(token)) {
            updateSession(updateSessionDTO, token, sessionId);
        } else {
            throw new InvalidTokenException("Unauthorized token is given", "UNAUTHORIZED_TOKEN");
        }
    }


    public boolean isEmptyOrNull(String s) {
        if (Objects.equals(s, "") || s == null) {
            return true;
        }
        return false;
    }

    public boolean isNull(Integer i) {
        if (i == null) {
            return true;
        }
        return false;
    }

    public boolean isNull(Float f) {
        if (f == null) {
            return true;
        }
        return false;
    }

    /**
     * Delete session by token and sessionId
     * Validate token, then check if token and sessionId belongs to the user of the token and sessionId
     * This method throws an exception on failure and returns normally on success.
     * @param token                         authentication token
     * @param sessionId                     id of the session to be deleted
     * @throws CustomException              if session doesn't exist
     * @throws SessionOwnershipException    if the token does not have access to the specified session
     * @throws InvalidTokenException        if token is Invalid
     */

    public void deleteSessionForUser(String token, int sessionId) {
        if (userDataRepository.doesTokenExist(token)) {
            if (doesTokenMatchUser(token, sessionId)) {
                int valueOfDeleteSessionQuery = userDataRepository.deleteSession(sessionId);
                if (valueOfDeleteSessionQuery == 0) {
                    throw new CustomException("Session couldn't be deleted", "NON_EXISTENT_SESSION_ID");
                }
            }
            else {
                throw new SessionOwnershipException("Invalid token or sessionId, therefore can't delete session", "INVALID_TOKEN_SESSION_ID");
            }
        } else {
            throw new InvalidTokenException("Token couldn't be verified", "UNAUTHORIZED_TOKEN");
        }
    }

}
