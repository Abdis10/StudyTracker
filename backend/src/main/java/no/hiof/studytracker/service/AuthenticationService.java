package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.TokenValidationResponse;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.repository.UserDataRepository;

import java.sql.Timestamp;
import java.util.Objects;

public class AuthenticationService {
    UserDataRepository userDataRepository;

    public AuthenticationService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public TokenValidationResponse isSessionValid(String token) {
        if (userDataRepository.doesTokenExist(token)) {
            Timestamp expiresAtTimestamp = userDataRepository.getSessionTokenIdExpiresAt(token);
            if (expiresAtTimestamp != null && expiresAtTimestamp.after(new Timestamp(System.currentTimeMillis()))) {
                int userId = userDataRepository.getUserIdByToken(token);

                // Hent verdier og sørg for at de ikke er null (Null-safety)
                String email = Objects.requireNonNullElse(userDataRepository.getEmailByUserId(userId), "");
                String username = Objects.requireNonNullElse(userDataRepository.getUsernameByUserid(userId), "ukjent_bruker");
                String firstname = Objects.requireNonNullElse(userDataRepository.getUserFirstname(userId), "");

                return new TokenValidationResponse(username, firstname, userId, email);
            }
        } else {
            throw new InvalidTokenException("Token doesn't exist", "INVALID_TOKEN");
        }

        throw new InvalidTokenException("Session token have expired", "EXPIRED_SESSION_TOKEN");
    }

}


