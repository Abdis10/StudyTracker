package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.SessionDataDTO;
import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.exceptions.SessionOwnershipException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.repository.UserDataRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class SessionService {

    private final UserDataRepository userDataRepository;

    public SessionService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    // =========================
    // VALIDATION
    // =========================

    public void validateSessionData(SessionDataDTO dto) {

        String token = dto.getToken();

        // FAIL FAST
        if (token == null || token.isBlank()) {
            throw new CustomException("Missing token", "MISSING_TOKEN");
        }

        if (!userDataRepository.doesTokenExist(token)) {
            throw new CustomException("Token couldn't be verified", "UNIDENTIFIED_TOKEN");
        }

        List<Map<String, String>> errors = new ArrayList<>();

        if (dto.getDate() == null || dto.getDate().isBlank()) {
            errors.add(Map.of("field", "date", "message", "Date is required"));
        } else {
            try {
                LocalDateTime.parse(dto.getDate() + "T00:00:00");
            } catch (Exception e) {
                errors.add(Map.of("field", "date", "message", "Invalid format (yyyy-MM-dd)"));
            }
        }

        if (dto.getHours() <= 0) {
            errors.add(Map.of("field", "hours", "message", "Must be greater than 0"));
        } else if (dto.getHours() > 24) {
            errors.add(Map.of("field", "hours", "message", "Cannot exceed 24"));
        }

        if (dto.getProductivityScore() < 1 || dto.getProductivityScore() > 10) {
            errors.add(Map.of("field", "productivityScore", "message", "Must be between 1 and 10"));
        }

        if (dto.getComment() != null && dto.getComment().length() > 255) {
            errors.add(Map.of("field", "comment", "message", "Max 255 characters"));
        }

        // SUBJECT (kun hvis ingen andre feil – spar DB kall)
        if (errors.isEmpty() && dto.getSubjectId() != null) {
            int userId = userDataRepository.getUserIdByToken(token);

            boolean subjectExists = userDataRepository
                    .getSubjectsByUser(userId)
                    .stream()
                    .anyMatch(s -> s.getId() == dto.getSubjectId());

            if (!subjectExists) {
                errors.add(Map.of("field", "subjectId", "message", "Invalid subject for user"));
            }
        }

        if (!errors.isEmpty()) {
            throw new CustomException(errors);
        }
    }

    // =========================
    // CREATE SESSION
    // =========================

    public void createStudySession(SessionDataDTO dto) {

        String token = dto.getToken();
        int userId = userDataRepository.getUserIdByToken(token);

        Timestamp createdAt = Timestamp.from(Instant.now());
        dto.setCreatedAt(createdAt);

        Session session = new Session(
                userId,
                dto.getDate(),
                dto.getHours(),
                dto.getProductivityScore(),
                dto.getComment(),
                createdAt,
                dto.getSubjectId()
        );

        userDataRepository.registerStudySession(session);
    }

    public void studySession(SessionDataDTO dto) {
        validateSessionData(dto);
        createStudySession(dto);
    }

    // =========================
    // TOKEN VALIDATION
    // =========================

    public boolean validateToken(String token) {
        return userDataRepository.doesTokenExist(token);
    }

    // =========================
    // FETCH SESSIONS
    // =========================

    public List<SessionResponseDTO> getSessionsFromRepository(String token) {

        int userId = userDataRepository.getUserIdByToken(token);

        return userDataRepository.getSessions(userId)
                .stream()
                .sorted(Comparator.comparing(SessionResponseDTO::getDate,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();
    }

    public List<SessionResponseDTO> getSessions(String token) {
        if (validateToken(token)) {
            return getSessionsFromRepository(token);
        }
        throw new CustomException("Invalid token", "UNAUTHORIZED_TOKEN");
    }

    // =========================
    // OWNERSHIP VALIDATION
    // =========================

    public boolean doesTokenMatchUser(String token, int sessionId) {
        int userIdByToken = userDataRepository.getUserIdByToken(token);
        int userIdBySessionId = userDataRepository.getUserIdBySessionId(sessionId);
        return userIdByToken == userIdBySessionId;
    }

    // =========================
    // UPDATE SESSION
    // =========================

    public boolean updateSession(UpdateSessionDTO dto, String token, int sessionId) {

        if (!doesTokenMatchUser(token, sessionId)) {
            throw new SessionOwnershipException("Given token and session-id doesn't match user", "INVALID_TOKEN_SESSION_ID");
        }

        UpdateSessionDTO existing = userDataRepository.getSessionBySessionId(sessionId);

        UpdateSessionDTO merged = new UpdateSessionDTO();

        merged.setUpdatedAt(Timestamp.from(Instant.now()));
        merged.setComment(isEmptyOrNull(dto.getComment()) ? existing.getComment() : dto.getComment());
        merged.setHours(dto.getHours() == null ? existing.getHours() : dto.getHours());

        merged.setDate(isEmptyOrNull(dto.getDate()) ? existing.getDate() : dto.getDate());

        merged.setProductivityScore(dto.getProductivityScore() == null
                ? existing.getProductivityScore()
                : dto.getProductivityScore());

        merged.setSubjectId(dto.getSubjectId() == null
                ? existing.getSubjectId()
                : dto.getSubjectId());

        merged.setCreatedAt(existing.getCreatedAt());

        int updatedRows = userDataRepository.updateSession(sessionId, merged);

        if (updatedRows == 0) {
            throw new CustomException("Session does not exist", "NON_EXISTENT_SESSION");
        }

        return true;
    }

    public void updateSessionInRepo(UpdateSessionDTO dto, String token, int sessionId) {
        if (!userDataRepository.doesTokenExist(token)) {
            throw new InvalidTokenException("Unauthorized token is given", "UNAUTHORIZED_TOKEN");
        }
        updateSession(dto, token, sessionId);
    }

    // =========================
    // DELETE SESSION
    // =========================

    public void deleteSessionForUser(String token, int sessionId) {

        if (!userDataRepository.doesTokenExist(token)) {
            throw new InvalidTokenException("Token couldn't be verified", "UNAUTHORIZED_TOKEN");
        }

        if (!doesTokenMatchUser(token, sessionId)) {
            throw new SessionOwnershipException("Invalid token or sessionId", "INVALID_TOKEN_SESSION_ID");
        }

        int deleted = userDataRepository.deleteSession(sessionId);

        if (deleted == 0) {
            throw new CustomException("Session couldn't be deleted", "NON_EXISTENT_SESSION_ID");
        }
    }

    // =========================
    // HELPERS
    // =========================

    public boolean isEmptyOrNull(String s) {
        return s == null || s.isEmpty();
    }
}