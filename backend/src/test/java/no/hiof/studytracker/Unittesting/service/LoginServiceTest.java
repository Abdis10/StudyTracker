package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.DTOs.LoginResponseDTO;
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

/**
 * Testklasse for {@link LoginService}.
 *
 * <p>
 * Denne klassen inneholder enhetstester for autentisering og sesjonshåndtering
 * i service-laget. Testene fokuserer på korrekt validering av brukerdata,
 * sikker håndtering av passord og riktig samhandling med
 * {@link UserDataRepository}.
 * </p>
 *
 * <h2>Teststrategi</h2>
 * <ul>
 *     <li>Avhengigheter mot databaselaget isoleres ved bruk av {@link org.mockito.Mock}.</li>
 *     <li>{@link org.mockito.Spy} benyttes der intern metodekall må kontrolleres,
 *         for eksempel ved opprettelse av sesjonstoken.</li>
 *     <li>Repository-adferd stubbes for å simulere ulike autentiseringsscenarioer.</li>
 * </ul>
 *
 * <h2>Hva som testes</h2>
 * <ul>
 *     <li><b>Autentisering</b>
 *         <ul>
 *             <li>Vellykket autentisering med gyldig e-post og passord</li>
 *             <li>Feil passord returnerer {@code false}</li>
 *             <li>Ukjent e-post resulterer i {@link UserAuthenticationException}</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Opprettelse av sesjonstoken</b>
 *         <ul>
 *             <li>Sesjonstoken opprettes når autentisering er vellykket</li>
 *             <li>Sesjonstoken lagres i repository</li>
 *             <li>Exception kastes dersom autentisering feiler</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Designprinsipper</h2>
 * <ul>
 *     <li>Hver test verifiserer én tydelig forretningsregel.</li>
 *     <li>Happy path og feilstier testes separat.</li>
 *     <li>Sikkerhetskritisk logikk (autentisering og token-håndtering) testes eksplisitt.</li>
 *     <li>Bivirkninger verifiseres ved bruk av {@code verify()}.</li>
 * </ul>
 *
 * <p>
 * Testene er utformet for å gi tilstrekkelig dekning for en MVP-løsning,
 * med fokus på korrekthet, sikkerhet og vedlikeholdbarhet.
 * </p>
 */

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

        when(mockUserDataRepository.getId(email)).thenReturn(1);
        when(mockUserDataRepository.getUserFirstname(1)).thenReturn("Ibrahim");
        when(mockUserDataRepository.getUsernameByUserid(1)).thenReturn("IbraUser");

        // act
        LoginResponseDTO response = spyLogicService.createSessionToken(email, pw);

        // assert
        verify(mockUserDataRepository)
                .saveSessionToken(anyString(), anyInt(), anyString(), anyString());

        Assertions.assertEquals("Ibrahim", response.firstname());
        Assertions.assertEquals("IbraUser", response.username());
        Assertions.assertEquals(email, response.email());
        Assertions.assertNotNull(response.token());
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
