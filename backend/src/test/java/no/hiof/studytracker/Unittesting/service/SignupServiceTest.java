package no.hiof.studytracker.Unittesting.service;

import no.hiof.studytracker.DTOs.SignupDTO;
import no.hiof.studytracker.repository.UserDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {

    @Mock
    SignupDTO mockSignupDTO;

    @Mock
    UserDataRepository mockUserDataRepository;

    @Test
    void validateSignupData(SignupDTO signupDTO) {
        // arrange

        // act

        // assert
    }
}

