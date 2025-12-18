package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.exceptions.UserAuthenticationException;
import main.java.no.hiof.studytracker.model.SessionToken;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
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
            }
        }
        else {
            throw new UserAuthenticationException(email);
        }

        return false;
    }

    public String createSessionToken(String email, String password) {
        if (authenticateUser(email, password)) {
            String token = UUID.randomUUID().toString();
            int userID = Integer.parseInt(userDataRepository.getId(email));
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiresAt = createdAt.plusHours(1);
            SessionToken sessionToken = new SessionToken(token, userID, createdAt.toString(), expiresAt.toString());
            userDataRepository.saveSessionToken(sessionToken.getSessionTokenId(), sessionToken.getUserId(),
                    sessionToken.getCreatedAt(), sessionToken.getExpiresAt());

            return userDataRepository.sessionTokenId(userID);
        }

        return "Unsuccessfull session token creation!";
    }

    public String getSessionTokenId(String token) {
        return token;
    }


}
