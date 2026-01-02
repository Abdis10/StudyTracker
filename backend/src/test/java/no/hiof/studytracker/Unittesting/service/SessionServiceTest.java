package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.DTOs.SessionDataDTO;
import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

}
