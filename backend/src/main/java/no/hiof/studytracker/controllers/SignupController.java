package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.exceptions.EmailAlreadyExistsException;
import main.java.no.hiof.studytracker.exceptions.InvalidEmailFormatException;
import main.java.no.hiof.studytracker.exceptions.InvalidPasswordException;
import main.java.no.hiof.studytracker.exceptions.UsernameAlreadyExistsException;
import main.java.no.hiof.studytracker.model.Errortype;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.SignupResult;
import main.java.no.hiof.studytracker.service.SignupService;

import java.util.Map;

public class SignupController {
    private SignupService signupService;
    private UserDataRepository userDataRepository;

    public SignupController(SignupService signupService, UserDataRepository userDataRepository) {
        this.signupService = signupService;
        this.userDataRepository = userDataRepository;
    }

    public void signupUser(Context ctx) {
        SignupDTO signupDTO = ctx.bodyAsClass(SignupDTO.class);

        try {
            signupService.signup(signupDTO);
            ctx.status(200).result("User registered successfully.");
        }

        catch (UsernameAlreadyExistsException e) {
            ctx.status(409).json(Map.of(
                    "error", e.getMessage(),
                    "username", e.getUsername()
            ));

        } catch (EmailAlreadyExistsException e) {
            ctx.status(409).json(Map.of(
                    "error", e.getMessage(),
                    "email", e.getEmail()
            ));

        } catch (InvalidEmailFormatException | InvalidPasswordException e) {
            ctx.status(400).json(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            // Felles fallback
            ctx.status(500).json("An unexpected error occurred");
        }

    }
}
