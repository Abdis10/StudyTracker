package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.DTOs.SignupDTO;
import no.hiof.studytracker.exceptions.EmailAlreadyExistsException;
import no.hiof.studytracker.exceptions.InvalidEmailFormatException;
import no.hiof.studytracker.exceptions.InvalidPasswordException;
import no.hiof.studytracker.exceptions.UsernameAlreadyExistsException;
import no.hiof.studytracker.model.User;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.SignupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * Testklasse for {@link SignupService}.
 *
 * <p>
 * Denne klassen inneholder enhetstester for registreringslogikk i service-laget.
 * Testene fokuserer på validering av brukerinput, håndtering av duplikater
 * (brukernavn og e-post), samt korrekt opprettelse av nye brukere.
 * </p>
 *
 * <h2>Teststrategi</h2>
 * <ul>
 *     <li>Avhengigheter mot databaselaget isoleres ved bruk av {@link org.mockito.Mock}.</li>
 *     <li>Repository-kall stubbes for å simulere eksisterende brukere.</li>
 *     <li>Valideringslogikk testes uten eksterne avhengigheter.</li>
 * </ul>
 *
 * <h2>Hva som testes</h2>
 * <ul>
 *     <li><b>Validering av brukernavn</b>
 *         <ul>
 *             <li>Exception kastes dersom brukernavn allerede eksisterer</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Validering av e-post</b>
 *         <ul>
 *             <li>Exception kastes dersom e-post allerede eksisterer</li>
 *             <li>Exception kastes ved ugyldig e-postformat</li>
 *             <li>Ingen exception ved gyldig e-postformat</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Validering av passord</b>
 *         <ul>
 *             <li>Exception kastes dersom passordet er kortere enn minimumslengde</li>
 *             <li>Ingen exception ved gyldig passordlengde</li>
 *         </ul>
 *     </li>
 *
 *     <li><b>Opprettelse av bruker</b>
 *         <ul>
 *             <li>Bruker lagres i repository når registrering er vellykket</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Designprinsipper</h2>
 * <ul>
 *     <li>Hver test verifiserer én tydelig forretningsregel.</li>
 *     <li>Happy path og feilstier testes separat.</li>
 *     <li>Valideringslogikk testes før persistens.</li>
 *     <li>Bivirkninger (lagring i database) verifiseres ved bruk av {@code verify()}.</li>
 * </ul>
 *
 * <p>
 * Testene er utformet for å gi tilstrekkelig dekning for en MVP-løsning,
 * med fokus på korrekthet, lesbarhet og vedlikeholdbarhet.
 * </p>
 */

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {

    @Mock
    UserDataRepository mockUserDataRepository;

    @InjectMocks
    SignupService signupService;

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345@example.com");
        // Eks. av input verdier fra bruker
        String username = signupDTO.getUsername();

        Mockito.when(mockUserDataRepository.usernameExists(username)).thenReturn(true);

        // act + assert
        Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> {
            signupService.validateSignupData(signupDTO);
        });
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {

        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345@example.com");
        String email = signupDTO.getEmail();

        Mockito.when(mockUserDataRepository.emailExists(email)).thenReturn(true);

        // act + assert
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            signupService.validateSignupData(signupDTO);
        });
    }

    @Test
    public void shouldThrowExceptionWhenInvalidEmailFormat() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345example.com");

        // act + assert
        Assertions.assertThrows(InvalidEmailFormatException.class, () -> {
            signupService.validateSignupData(signupDTO);
        });
    }

    @Test
    public void shouldNotThrowExceptionWhenValidEmailFormat() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345@example.com");
        // setter password for å unngå at if-setningen som sjekker password-lengde i signupService kaster nullpointerException
        signupDTO.setPassword("StrongPass123");

        // act + assert
        Assertions.assertDoesNotThrow( () -> signupService.validateSignupData(signupDTO));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordLengthIsLessThenEightCharacters() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345@example.com");
        signupDTO.setPassword("Strong");

        // act + assert
        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            signupService.validateSignupData(signupDTO);
        });
    }

    @Test
    public void shouldNotThrowExceptionWhenPasswordLengthIsMoreThanEightCharacters() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Ibra10", "Ibra12345@example.com");
        signupDTO.setPassword("StrongPass123");

        // act + assert
        Assertions.assertDoesNotThrow( () -> signupService.validateSignupData(signupDTO));
    }


    @Test
    public void shouldCreateUser() {
        // arrange
        SignupDTO signupDTO = new SignupDTO("Mohammed", "Ahmed","Moha07",  "Moha123@example.com", "Moha123", "Male");

        // act
        signupService.registerUser(signupDTO);

        // assert
        Mockito.verify(mockUserDataRepository).saveUser(any(User.class));
    }
 }

