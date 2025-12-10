package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.model.User;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

import java.util.ArrayList;

public class SignupService {
    private SignupDTO signupDTO;
    private UserDataRepository userDataRepository;
    private boolean usernameExists = false;
    private boolean emailExists = false;
    private boolean userRegistered = false;

    public SignupService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public void getSignupData(SignupDTO signupDTO) {
        this.signupDTO = signupDTO;
    }


    public boolean validateSignupData() {
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (userDataRepository.usernameExists(username)) {
            usernameExists = true;
            return true;
        }

        if (userDataRepository.emailExists(email)) {
            emailExists = true;
            return true;
        }

        else {
            usernameExists = false;
            emailExists = false;
            return false;
        }
    }

    public String registerUser() {
        if (validateSignupData() && usernameExists) {
            return "Brukernavn eksisterer, vennligst velg nytt!";
        }

        if (validateSignupData() && emailExists) {
            return "Mailadressen er allerede i bruk!";
        }

        if (!validateSignupData()) {
            String pw = signupDTO.getPassword();
            String hashedPw = PasswordUtil.hashPw(pw);
            SignupDTO signupDTO1 = new SignupDTO(signupDTO.getFirstname(), signupDTO.getLastname(),
                    signupDTO.getUsername(), signupDTO.getEmail(), hashedPw, signupDTO.getGender()
            );
            String firstname = signupDTO1.getFirstname();
            String lastname = signupDTO1.getLastname();
            String signupDTO1Username = signupDTO1.getUsername();
            String signupDTO1Email = signupDTO1.getEmail();
            String password = signupDTO1.getPassword();
            String gender = signupDTO1.getGender();


            User user = new User(firstname, lastname, signupDTO1Username, signupDTO1Email, password, gender);
            userDataRepository.saveUser(user);
        }
        userRegistered = true;
        return "Bruker har blitt registerert.";
    }

    public boolean isUserRegistered() {
        return userRegistered;
    }
}
