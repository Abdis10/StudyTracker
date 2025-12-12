package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.repository.UserDataRepository;
import org.mindrot.jbcrypt.BCrypt;

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

    public String authenticatePassword() {
        return userDataRepository.passwordHashExists();
    }



}
