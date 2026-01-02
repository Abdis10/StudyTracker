package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.UserAuthenticationException;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.LoginService;
import no.hiof.studytracker.service.PasswordUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    UserDataRepository mockUserDataRepository;

    @InjectMocks
    LoginService loginService;

    @Test
    public void shouldReturnTrueIfUserIsAuthenticated() {
        // arrange
        String email = "Ibra12345@example.com";
        String pw = "StrongPass123";
        String hashPw = PasswordUtil.hashPw(pw);
        Mockito.when(mockUserDataRepository.emailExists(email)).thenReturn(true);
        Mockito.when(mockUserDataRepository.getPasswordHash(email)).thenReturn(hashPw);

        // act
        boolean result = loginService.authenticateUser(email, pw);

        // assert
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordIsIncorrect() {
        // arrange
        String email = "Ibra12345@example.com";
        String pw = "StrongPass123";
        String falsePw = "strongpass123";
        String hashOfDifferentPassword = PasswordUtil.hashPw(falsePw);
        Mockito.when(mockUserDataRepository.emailExists(email)).thenReturn(true);
        Mockito.when(mockUserDataRepository.getPasswordHash(email)).thenReturn(hashOfDifferentPassword);

        // act
        boolean result = loginService.authenticateUser(email, pw);

        // assert
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldThrowExceptionWhenEmailDoesNotExist() {
        // arrange
        String email = "Ibra12345@example.com";
        String pw = "StrongPass123";
        Mockito.when(mockUserDataRepository.emailExists(email)).thenReturn(false);

        // act + assert
        Assertions.assertThrows(UserAuthenticationException.class, () -> {
           loginService.authenticateUser(email, pw);
        });
        Mockito.verify(mockUserDataRepository, never()).getPasswordHash(any());
    }

    @Spy
    @InjectMocks
    LoginService spyLogicService;

    @Test
    public void shouldCreateSessionToken() {
        // arrange
        String email = "Ibra12345@example.com";
        String pw = "StrongPass123";
        doReturn(true).when(spyLogicService).authenticateUser(email, pw);
        when(mockUserDataRepository.getId(email)).thenReturn("1");
        when(mockUserDataRepository.sessionTokenId(1)).thenReturn("token");

        // act
        String token = spyLogicService.createSessionToken(email, pw);

        // assert
        verify(mockUserDataRepository).saveSessionToken(anyString(), anyInt(), anyString(), anyString());

        Assertions.assertEquals("token", token);
    }

    @Test
    public  void shouldThrowExceptionWhenPasswordIsInvalid() {
        // arrange
        String email = "Ibra12345@example.com";
        String pw = "StrongPass123";
        doReturn(false).when(spyLogicService).authenticateUser(email, pw);

        // act + assert
        Assertions.assertThrows(CustomException.class, () -> {
            spyLogicService.createSessionToken(email, pw);
        });
        verify(mockUserDataRepository, never()).saveSessionToken(anyString(), anyInt(), anyString(), anyString());
    }
}
