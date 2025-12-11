package main.java.no.hiof.studytracker.service;

import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.exceptions.EmailAlreadyExistsException;
import main.java.no.hiof.studytracker.exceptions.InvalidEmailFormatException;
import main.java.no.hiof.studytracker.exceptions.InvalidPasswordException;
import main.java.no.hiof.studytracker.exceptions.UsernameAlreadyExistsException;
import main.java.no.hiof.studytracker.model.Errortype;
import main.java.no.hiof.studytracker.model.User;
import main.java.no.hiof.studytracker.repository.UserDataRepository;

public class SignupService {
    private final UserDataRepository userDataRepository;

    public SignupService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }


    public void validateSignupData(SignupDTO signupDTO) {
        SignupResult result = new SignupResult();
        String username = signupDTO.getUsername();
        String email = signupDTO.getEmail();

        if (userDataRepository.usernameExists(username)) {
            /*result.setSuccess(false);
            result.setErrorType(Errortype.USERNAME_EXISTS);
            result.setMessage("Username is taken!");*/
            throw new UsernameAlreadyExistsException(username);
            //return result;
        }

        if (userDataRepository.emailExists(email)) {
            /*result.setSuccess(false);
            result.setErrorType(Errortype.EMAIL_EXISTS);
            result.setMessage("Email already in use!");
            return result;*/
            throw new EmailAlreadyExistsException(email);
        }

        /*result.setSuccess(true);
        result.setErrorType(Errortype.NONE);
        result.setMessage("Account is ready to be created.");
        return result;*/

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
