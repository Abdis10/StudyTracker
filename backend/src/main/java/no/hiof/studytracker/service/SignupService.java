package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

import java.util.ArrayList;

public class SignupService {
    private SignupDTO signupDTO;
    private UserDataRepository userDataRepository;
    private Boolean userExists = false;

    public SignupService(SignupDTO signupDTO, UserDataRepository userDataRepository) {
        this.signupDTO = signupDTO;
        this.userDataRepository = userDataRepository;
    }

    public void getSignupData(SignupDTO signupDTO) {
        this.signupDTO = signupDTO;
    }

    public String validateSignupData() {
        // Vi sjekker først om username eksisterer
        ArrayList<SignupDTO> listUsernameEmail = userDataRepository.getUsernameEmail();
        String username = "";
        String email = "";
        for (SignupDTO existing : listUsernameEmail) {
            username = existing.getUsername();
            email = existing.getEmail();

            if (signupDTO.getUsername().equals(username)) {
                userExists = true;
                return "Brukernavn eksisterer, vennligst velg nytt!";
            }

            if (signupDTO.getEmail().equals(email)) {
                userExists = true;
                return "Mailadressen er allerede i bruk!";
            }
        }


        userExists = false;
        return "Bruker har unik brukernavn og mailadresse";
    }

    public Boolean getUserExists() {
        return userExists;
    }

}
