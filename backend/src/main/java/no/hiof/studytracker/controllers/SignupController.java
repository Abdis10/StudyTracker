package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.model.Errortype;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.SignupResult;
import main.java.no.hiof.studytracker.service.SignupService;

public class SignupController {
    private SignupService signupService;
    private UserDataRepository userDataRepository;

    public SignupController(SignupService signupService, UserDataRepository userDataRepository) {
        this.signupService = signupService;
        this.userDataRepository = userDataRepository;
    }

    public void signupUser(Context ctx) {
        SignupDTO signupDTO = ctx.bodyAsClass(SignupDTO.class);

        SignupResult result = signupService.signup(signupDTO);

        if (result.getErrorType() == Errortype.USERNAME_EXISTS) {
            ctx.status(409).result(result.getMessage());
        }

        else if (result.getErrorType() == Errortype.EMAIL_EXISTS) {
            ctx.status(409).result(result.getMessage());
        }

        else if (result.getErrorType() == Errortype.NONE) {
            ctx.status(201).result(result.getMessage());
        }
    }
}
