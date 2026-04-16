package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.DTOs.SessionDataDTO;
import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.exceptions.SessionOwnershipException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testklasse for {@link SessionService}.
 *
 * <p>
 * Denne klassen inneholder enhetstester for service-lagets logikk knyttet til
 * studieøkter. Testene fokuserer på validering av forretningsregler,
 * guard-logikk og korrekt samhandling med {@link UserDataRepository}.
 * </p>
 *
 * <h2>Teststrategi</h2>
 * <ul>
 *     <li>Avhengigheter isoleres ved bruk av {@link org.mockito.Mock}.</li>
 *     <li>{@link org.mockito.Spy} benyttes der intern metode-delegering
 *         i {@code SessionService} må verifiseres.</li>
 *     <li>Repository-adferd stubbes for å simulere ulike databasetilstander.</li>
 * </ul>
 *
 * <h2>Hva som testes</h2>
 * <ul>
 *     <li><b>Valideringslogikk</b>
 *         <ul>
 *             <li>Ugyldige øktdata (f.eks. negative timer)</li>
 *             <li>Validering av om token eksisterer</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Opprettelse av økter</b>
 *         <ul>
 *             <li>Opprettelse av studieøkter når token er gyldig</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Henting av økter</b>
 *         <ul>
 *             <li>Henting og sortering av økter etter dato (synkende rekkefølge)</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Eierskapsvalidering</b>
 *         <ul>
 *             <li>Match og mismatch mellom token og økteier</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Oppdatering av økter</b>
 *         <ul>
 *             <li>Vellykket oppdatering når token og eierskap er gyldig</li>
 *             <li>Exception når økten ikke eksisterer</li>
 *             <li>Exception når token ikke tilhører økteier</li>
 *             <li>Wrapper-metodeadferd i {@code updateSessionInRepo}</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Sletting av økter</b>
 *         <ul>
 *             <li>Vellykket sletting ved gyldig token og korrekt eierskap</li>
 *             <li>Exception når økten ikke eksisterer</li>
 *             <li>Exception når token ikke tilhører økteier</li>
 *             <li>Exception når token er ugyldig</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Designprinsipper</h2>
 * <ul>
 *     <li>Hver test verifiserer én forretningsregel.</li>
 *     <li>Happy path og feilstier testes separat.</li>
 *     <li>Bivirkninger (databaseoperasjoner) verifiseres ved bruk av {@code verify()}.</li>
 *     <li>Tidlig avbrudd valideres ved bruk av {@code verify(..., never())}.</li>
 * </ul>
 *
 * <p>
 * Testene er utformet for å gi tilstrekkelig dekning for en MVP-løsning,
 * med fokus på korrekthet, lesbarhet og vedlikeholdbarhet.
 * </p>
 */


@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    UserDataRepository mockUserDataRepository;

    @InjectMocks
    SessionService sessionService;

    @Spy
    @InjectMocks
    SessionService spySessionService;

    // =========================
    // VALIDATION TESTS
    // =========================

    @Test
    void shouldFailFastIfTokenIsMissing() {
        SessionDataDTO dto = new SessionDataDTO();

        CustomException ex = assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(dto);
        });

        assertEquals("MISSING_TOKEN", ex.getErrorCode());
        verify(mockUserDataRepository, never()).doesTokenExist(any());
    }

    @Test
    void shouldThrowExceptionIfTokenDoesNotExist() {
        SessionDataDTO dto = new SessionDataDTO();
        dto.setToken("token");

        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(dto);
        });

        assertEquals("UNIDENTIFIED_TOKEN", ex.getErrorCode());
        verify(mockUserDataRepository).doesTokenExist("token");
    }

    @Test
    void shouldReturnErrorIfHoursInvalid() {
        SessionDataDTO dto = new SessionDataDTO();
        dto.setToken("token");
        dto.setDate("2025-01-01");
        dto.setHours(-1);
        dto.setProductivityScore(5);

        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(dto);
        });

        assertNotNull(ex.getErrors());
        assertTrue(
                ex.getErrors().stream()
                        .anyMatch(e -> e.get("field").equals("hours"))
        );
    }

    @Test
    void shouldReturnMultipleValidationErrors() {
        SessionDataDTO dto = new SessionDataDTO();
        dto.setToken("token");
        dto.setDate("");
        dto.setHours(-1);
        dto.setProductivityScore(20);

        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(dto);
        });

        assertNotNull(ex.getErrors());
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void shouldReturnErrorIfSubjectInvalidForUser() {
        SessionDataDTO dto = new SessionDataDTO();
        dto.setToken("token");
        dto.setDate("2025-01-01");
        dto.setHours(2);
        dto.setProductivityScore(5);
        dto.setSubjectId(99);

        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);
        when(mockUserDataRepository.getUserIdByToken("token")).thenReturn(1);
        when(mockUserDataRepository.getSubjectsByUser(1)).thenReturn(new ArrayList<>());

        CustomException ex = assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(dto);
        });

        assertTrue(
                ex.getErrors().stream()
                        .anyMatch(e -> e.get("field").equals("subjectId"))
        );
    }

    // =========================
    // CREATE SESSION
    // =========================

    @Test
    void shouldCreateStudySession() {
        SessionDataDTO dto = new SessionDataDTO();
        dto.setToken("token");
        dto.setDate("2025-01-01");
        dto.setHours(2);
        dto.setProductivityScore(4);
        dto.setComment("Good session");
        dto.setSubjectId(2);

        when(mockUserDataRepository.getUserIdByToken("token")).thenReturn(1);

        sessionService.createStudySession(dto);

        verify(mockUserDataRepository).registerStudySession(any(Session.class));
    }

    // =========================
    // TOKEN
    // =========================

    @Test
    void shouldReturnTrueWhenTokenValid() {
        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);
        assertTrue(sessionService.validateToken("token"));
    }

    @Test
    void shouldReturnFalseWhenTokenInvalid() {
        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(false);
        assertFalse(sessionService.validateToken("token"));
    }

    // =========================
    // GET SESSIONS
    // =========================

    @Test
    void shouldReturnSessionsSortedByDateDesc() {
        String token = "token";
        int userId = 1;

        List<SessionResponseDTO> unsorted = List.of(
                dtoWithDate("2024-01-01"),
                dtoWithDate("2024-03-01"),
                dtoWithDate("2023-12-01")
        );

        when(mockUserDataRepository.getUserIdByToken(token)).thenReturn(userId);
        when(mockUserDataRepository.getSessions(userId)).thenReturn(unsorted);

        List<SessionResponseDTO> result = sessionService.getSessionsFromRepository(token);

        assertEquals("2024-03-01", result.get(0).getDate());
    }

    private SessionResponseDTO dtoWithDate(String date) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setDate(date);
        return dto;
    }

    // =========================
    // OWNERSHIP
    // =========================

    @Test
    void shouldReturnTrueIfTokenMatchesUser() {
        when(mockUserDataRepository.getUserIdByToken("token")).thenReturn(1);
        when(mockUserDataRepository.getUserIdBySessionId(1)).thenReturn(1);

        assertTrue(sessionService.doesTokenMatchUser("token", 1));
    }

    @Test
    void shouldReturnFalseIfTokenDoesNotMatchUser() {
        when(mockUserDataRepository.getUserIdByToken("token")).thenReturn(1);
        when(mockUserDataRepository.getUserIdBySessionId(1)).thenReturn(2);

        assertFalse(sessionService.doesTokenMatchUser("token", 1));
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void shouldUpdateSessionSuccessfully() {
        String token = "token";
        int sessionId = 1;

        UpdateSessionDTO dto = new UpdateSessionDTO();
        dto.setDate("2025-12-24");
        dto.setHours(3f);
        dto.setProductivityScore(8);
        dto.setComment("Nice");

        when(spySessionService.doesTokenMatchUser(token, sessionId)).thenReturn(true);
        when(mockUserDataRepository.getSessionBySessionId(sessionId)).thenReturn(dto);
        when(mockUserDataRepository.updateSession(anyInt(), any())).thenReturn(1);

        assertTrue(spySessionService.updateSession(dto, token, sessionId));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void shouldDeleteSessionSuccessfully() {
        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);
        doReturn(true).when(spySessionService).doesTokenMatchUser("token", 1);
        when(mockUserDataRepository.deleteSession(1)).thenReturn(1);

        assertDoesNotThrow(() -> spySessionService.deleteSessionForUser("token", 1));
    }

    @Test
    void shouldThrowIfDeleteFails() {
        when(mockUserDataRepository.doesTokenExist("token")).thenReturn(true);
        doReturn(true).when(spySessionService).doesTokenMatchUser("token", 1);
        when(mockUserDataRepository.deleteSession(1)).thenReturn(0);

        CustomException ex = assertThrows(CustomException.class, () -> {
            spySessionService.deleteSessionForUser("token", 1);
        });

        assertEquals("NON_EXISTENT_SESSION_ID", ex.getErrorCode());
    }
}