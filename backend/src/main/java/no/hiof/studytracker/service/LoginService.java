package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.LoginResponseDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.UserAuthenticationException;
import no.hiof.studytracker.model.SessionToken;
import no.hiof.studytracker.repository.UserDataRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

public class LoginService {
    private final UserDataRepository userDataRepository;

    public LoginService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public String authenticateEmail(String email) {
        if (userDataRepository.emailExists(email)) {
            return "Email is valid and exists on database";
        } else {
            return "Email doesn't exist on database";
        }
    }

    public boolean authenticateUser(String email, String password) {
        if (userDataRepository.emailExists(email)) {
            if (BCrypt.checkpw(password, userDataRepository.getPasswordHash(email))) {
                return true;
            } else {
                return false;
            }
        }
        else {
            throw new UserAuthenticationException();
        }
    }

    public LoginResponseDTO createSessionToken(String email, String password) {
        if (authenticateUser(email, password)) {
            String token = UUID.randomUUID().toString();
            int userID = userDataRepository.getId(email);

            Timestamp createdAt = Timestamp.from(Instant.now());
            TemporalAmount tma = Duration.ofMinutes(60);
            Timestamp expiresAt = Timestamp.from(createdAt.toInstant().plus(tma));
            SessionToken sessionToken = new SessionToken(token, userID, createdAt.toString(), expiresAt.toString());
            userDataRepository.saveSessionToken(sessionToken.getSessionTokenId(), sessionToken.getUserId(),
                    sessionToken.getCreatedAt(), sessionToken.getExpiresAt());

            String firstname = userDataRepository.getUserFirstname(userID);
            String username = userDataRepository.getUsernameByUserid(userID);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(firstname, username, email, token);
            return loginResponseDTO;
        }

        else {
            throw new CustomException("Couldn't validate user!", "INVALID_PASSWORD");
        }
    }

    public String getSessionTokenId(String token) {
        return token;
    }
}
