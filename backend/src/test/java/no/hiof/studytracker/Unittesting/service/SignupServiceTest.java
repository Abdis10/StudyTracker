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

