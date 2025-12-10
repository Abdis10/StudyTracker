package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.model.Errortype;
import main.java.no.hiof.studytracker.model.User;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

public class SignupService {
    private UserDataRepository userDataRepository;

    public SignupService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }


    public SignupResult validateSignupData(SignupDTO signupDTO) {
        SignupResult result = new SignupResult();
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (userDataRepository.usernameExists(username)) {
            result.setSuccess(false);
            result.setErrorType(Errortype.USERNAME_EXISTS);
            result.setMessage("Username is taken!");
            return result;
        }

        if (userDataRepository.emailExists(email)) {
            result.setSuccess(false);
            result.setErrorType(Errortype.EMAIL_EXISTS);
            result.setMessage("Email already in use!");
            return result;
        }

        result.setSuccess(true);
        result.setErrorType(Errortype.NONE);
        result.setMessage("Account is ready to be created.");
        return result;

    }

    public boolean registerUser(SignupDTO signupDTO) {
            String pw = signupDTO.getPassword();
            String hashedPw = PasswordUtil.hashPw(pw);

            String firstname = signupDTO.getFirstname();
            String lastname = signupDTO.getLastname();
            String username = signupDTO.getUsername();
            String email = signupDTO.getEmail();
            String gender = signupDTO.getGender();

            User user = new User(firstname, lastname, username, email, hashedPw, gender);
            userDataRepository.saveUser(user);
            return true;
    }

    public SignupResult signup(SignupDTO signupDTO) {
        SignupResult signupResult = validateSignupData(signupDTO);

        if (!signupResult.isSuccess()) {
            return signupResult;
        } else {
            registerUser(signupDTO);
            signupResult = new SignupResult(true, Errortype.NONE, "User successfully registered.");
            return signupResult;
        }

    }


}
