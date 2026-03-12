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

    @Test
    void shouldThrowExceptionIfHoursIsLessThanZero() {
        // arrange
        SessionDataDTO sessionDataDTO = new SessionDataDTO();
        sessionDataDTO.setHours(-1);

        // act + assert
        Assertions.assertThrows(CustomException.class, () -> {
           sessionService.validateSessionData(sessionDataDTO);
        });
        verify(mockUserDataRepository, never()).doesTokenExist(any());
    }

    @Test
    void shouldThrowExceptionIfTokenDoesNotExist() {
        // arrange
        SessionDataDTO sessionDataDTO = new SessionDataDTO();
        sessionDataDTO.setHours(10);
        sessionDataDTO.setToken("token");
        String token = sessionDataDTO.getToken();
        doReturn(false).when(mockUserDataRepository).doesTokenExist(token);
        // act + assert
        Assertions.assertThrows(CustomException.class, () -> {
            sessionService.validateSessionData(sessionDataDTO);
        });
        verify(mockUserDataRepository).doesTokenExist(token);
    }

    @Test
    void shouldCreateStudySession() {
        // arrange
        SessionDataDTO sessionDataDTO = new SessionDataDTO();
        sessionDataDTO.setToken("token");
        sessionDataDTO.setDate("2025-01-01");
        sessionDataDTO.setHours(2);
        sessionDataDTO.setProductivityScore(4);
        sessionDataDTO.setComment("Good session");

        when(mockUserDataRepository.getUserIdByToken(sessionDataDTO.getToken())).thenReturn(1);

        // act
        sessionService.createStudySession(sessionDataDTO);

        // assert
       verify(mockUserDataRepository).registerStudySession(any(Session.class));
    }

    @Test
    void shouldReturnTrueWhenTokenValid() {
        // arrange
        String token = "token";
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(true);
        // act
        boolean result = sessionService.validateToken(token);
        // assert
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenTokenInvalid() {
        // arrange
        String token = "token";
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(false);
        // act
        boolean result = sessionService.validateToken(token);
        // assert
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnSessions() {
        // arrange
        String token = "token";
        int userId = 1;

        List<SessionResponseDTO> unsorted = List.of(
                dtoWithDate("2024-01-01"),
                dtoWithDate("2024-03-01"),
                dtoWithDate("2023-12-01")
        );

        when(mockUserDataRepository.getUserIdByToken(token)).thenReturn(userId);
        when(mockUserDataRepository.getSessions(userId)).thenReturn(unsorted);

        // act
        List<SessionResponseDTO> result = sessionService.getSessionsFromRepository(token);

        // assert
       assertEquals(3, result.size());
       assertEquals("2024-03-01", result.get(0).getDate());
       assertEquals("2024-01-01", result.get(1).getDate());
       assertEquals("2023-12-01", result.get(2).getDate());
    }

    private SessionResponseDTO dtoWithDate(String date) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setDate(date);
        return dto;
    }

    @Test
    public void shouldReturnTrueIfTokenAndUserMatch() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.getUserIdByToken(token)).thenReturn(1);
        when(mockUserDataRepository.getUserIdBySessionId(sessionId)).thenReturn(1);

        // act
        boolean result  = sessionService.doesTokenMatchUser(token, sessionId);

        // assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfTokenAndUserDoNotMatch() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.getUserIdByToken(token)).thenReturn(1);
        when(mockUserDataRepository.getUserIdBySessionId(sessionId)).thenReturn(2);

        // act
        boolean result  = sessionService.doesTokenMatchUser(token, sessionId);

        // assert
        assertFalse(result);
    }

    @Spy
    @InjectMocks
    SessionService spySessionService;

    @Test
    void shouldReturnTrueIfTokenAndSessionAreValid() {
        // arrange
        String token = "token";
        int sessionId = 1;
        Timestamp createdAt = Timestamp.from(Instant.now());
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO("2025-12-24", 3.4f, 8, "Nice day", createdAt);
        when(spySessionService.doesTokenMatchUser(token, sessionId)).thenReturn(true);
        when(mockUserDataRepository.getSessionBySessionId(sessionId)).thenReturn(updateSessionDTO);
        when(mockUserDataRepository.updateSession(anyInt(), any(UpdateSessionDTO.class))).thenReturn(1);

        // act
        boolean result = spySessionService.updateSession(updateSessionDTO, token, sessionId);

        // assert
        assertTrue(result);
        verify(mockUserDataRepository).updateSession(anyInt(), any(UpdateSessionDTO.class));
    }

    @Test
    void shouldThrowExceptionWhenSessionDoesNotExist() {
        // arrange
        String token = "token";
        int sessionId = 1;
        Timestamp createdAt = Timestamp.from(Instant.now());
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO("2025-12-24", 3.4f, 8, "Nice day", createdAt);
        when(spySessionService.doesTokenMatchUser(token, sessionId)).thenReturn(true);
        when(mockUserDataRepository.getSessionBySessionId(sessionId)).thenReturn(updateSessionDTO);
        when(mockUserDataRepository.updateSession(anyInt(), any(UpdateSessionDTO.class))).thenReturn(0);

        // act + assert
        CustomException result = assertThrows(CustomException.class, () -> {
            spySessionService.updateSession(updateSessionDTO, token, sessionId);
        });

        assertEquals("NON_EXISTENT_SESSION", result.getErrorCode());
        verify(mockUserDataRepository).updateSession(anyInt(), any(UpdateSessionDTO.class));
    }

    @Test
    void shouldThrowExceptionIfSessionOwnershipIsInvalid() {
        // arrange
        String token = "token";
        int sessionId = 1;
        Timestamp createdAt = Timestamp.from(Instant.now());
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO("2025-12-24", 3.4f, 8, "Nice day", createdAt);
        when(spySessionService.doesTokenMatchUser(token, sessionId)).thenReturn(false);

        // act + assert
        SessionOwnershipException result = assertThrows(SessionOwnershipException.class, () -> {
            spySessionService.updateSession(updateSessionDTO, token, sessionId);
        });
        assertEquals("INVALID_TOKEN_SESSION_ID", result.getErrorCode());
        verify(mockUserDataRepository, never()).updateSession(anyInt(), any(UpdateSessionDTO.class));
    }

    @Test
    void shouldUpdateSessionInRepoIfTokenExists() {
        // arrange
        String token = "token";
        int sessionId = 1;
        Timestamp createdAt = Timestamp.from(Instant.now());
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO("2025-12-24", 3.4f, 8, "Nice day", createdAt);
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(true);
        doReturn(true).when(spySessionService).updateSession(updateSessionDTO, token, sessionId);
        // act
        spySessionService.updateSessionInRepo(updateSessionDTO, token, sessionId);
        // assert
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService).updateSession(updateSessionDTO, token, sessionId);
    }

    @Test
    void shouldThrowExceptionIfTokenIsInvalid() {
        // arrange
        String token = "token";
        int sessionId = 1;
        Timestamp createdAt = Timestamp.from(Instant.now());
        UpdateSessionDTO updateSessionDTO = new UpdateSessionDTO("2025-12-24", 3.4f, 8, "Nice day", createdAt);
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(false);

        // act + assert
        InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> {
            spySessionService.updateSessionInRepo(updateSessionDTO, token, sessionId);
        });
        assertEquals("UNAUTHORIZED_TOKEN", result.getErrorCode());
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService, never()).updateSession(updateSessionDTO, token, sessionId);
    }

    @Test
    void shouldDeleteSessionIfValidTokenAndTokenMatchesUser() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(true);
        doReturn(true).when(spySessionService).doesTokenMatchUser(token, sessionId);
        when(mockUserDataRepository.deleteSession(sessionId)).thenReturn(1);

        // act + assert
        assertDoesNotThrow(() -> spySessionService.deleteSessionForUser(token, sessionId));
        verify(mockUserDataRepository).deleteSession(sessionId);
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService).doesTokenMatchUser(token, sessionId);
    }

    @Test
    void shouldThrowExceptionIfSessionDoNotExist() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(true);
        doReturn(true).when(spySessionService).doesTokenMatchUser(token, sessionId);
        when(mockUserDataRepository.deleteSession(sessionId)).thenReturn(0);

        // act + assert
        CustomException result = assertThrows(CustomException.class, () -> {
           spySessionService.deleteSessionForUser(token, sessionId);
        });
        assertEquals("NON_EXISTENT_SESSION_ID", result.getErrorCode());
        verify(mockUserDataRepository).deleteSession(sessionId);
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService).doesTokenMatchUser(token, sessionId);
    }

    @Test
    void shouldThrowExceptionIfTokenDoNotMatchUser() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(true);
        doReturn(false).when(spySessionService).doesTokenMatchUser(token, sessionId);

        // act + assert
        SessionOwnershipException result = assertThrows(SessionOwnershipException.class, () -> {
            spySessionService.deleteSessionForUser(token, sessionId);
        });
        assertEquals("INVALID_TOKEN_SESSION_ID", result.getErrorCode());
        verify(mockUserDataRepository, never()).deleteSession(sessionId);
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService).doesTokenMatchUser(token, sessionId);
    }

    @Test
    void shouldThrowExceptionIfTokenInvalidToken() {
        // arrange
        String token = "token";
        int sessionId = 1;
        when(mockUserDataRepository.doesTokenExist(token)).thenReturn(false);

        // act + assert
        InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> {
            spySessionService.deleteSessionForUser(token, sessionId);
        });
        assertEquals("UNAUTHORIZED_TOKEN", result.getErrorCode());
        verify(mockUserDataRepository, never()).deleteSession(sessionId);
        verify(mockUserDataRepository).doesTokenExist(token);
        verify(spySessionService, never()).doesTokenMatchUser(token, sessionId);
    }

}

