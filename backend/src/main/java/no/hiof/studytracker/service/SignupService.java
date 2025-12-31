package no.hiof.studytracker.service;

import no.hiof.studytracker.DTOs.SignupDTO;
import no.hiof.studytracker.exceptions.EmailAlreadyExistsException;
import no.hiof.studytracker.exceptions.InvalidEmailFormatException;
import no.hiof.studytracker.exceptions.InvalidPasswordException;
import no.hiof.studytracker.exceptions.UsernameAlreadyExistsException;
import no.hiof.studytracker.model.User;
import no.hiof.studytracker.repository.UserDataRepository;

public class SignupService {
    private final UserDataRepository userDataRepository;

    public SignupService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }


    public void validateSignupData(SignupDTO signupDTO) {
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (userDataRepository.usernameExists(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        if (userDataRepository.emailExists(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailFormatException(email);
        }

        if (signupDTO.getPassword().length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters.");
        }
    }

    public void registerUser(SignupDTO signupDTO) {
            String pw = signupDTO.getPassword();
            String hashedPw = PasswordUtil.hashPw(pw);

            String firstname = signupDTO.getFirstname();
            String lastname = signupDTO.getLastname();
            String username = signupDTO.getUsername();
            String email = signupDTO.getEmail();
            String gender = signupDTO.getGender();

            User user = new User(firstname, lastname, username, email, hashedPw, gender);
            userDataRepository.saveUser(user);
    }

    public void signup(SignupDTO signupDTO) {
        validateSignupData(signupDTO);
        registerUser(signupDTO);
    }

}
