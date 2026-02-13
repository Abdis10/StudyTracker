package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.TokenValidationResponse;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.repository.UserDataRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class AuthenticationService {
    UserDataRepository userDataRepository;

    public AuthenticationService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public TokenValidationResponse isSessionValid(String token) {
        // tiden som har gått siden 1970 for currentTime
        long x = Instant.now().getEpochSecond();

        // tiden som har gått siden 1970 for expiresAt
        if (userDataRepository.doesTokenExist(token)) {
            Instant expiresAt = Instant.parse(userDataRepository.getSessionTokenIdExpiresAt(token));
            long y = expiresAt.getEpochSecond();
            if (x < y) {
                int userId = userDataRepository.getUserIdByToken(token);
                String email = userDataRepository.getEmailByUserId(userId);
                String username = userDataRepository.getUsernameByUserid(userId);
                String firstname = userDataRepository.getUserFirstname(userId);
                TokenValidationResponse validationResponse = new TokenValidationResponse(username, firstname, userId, email);
                return validationResponse;
            }
        } else {
            throw new InvalidTokenException("Token doesn't exist", "INVALID_TOKEN");
        }

        throw new InvalidTokenException("Session token have expired", "EXPIRED_SESSION_TOKEN");
    }

}


