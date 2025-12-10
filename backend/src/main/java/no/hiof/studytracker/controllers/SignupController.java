package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.SignupService;

public class SignupController {
    private SignupService signupService;
    private UserDataRepository userDataRepository;
    private SignupDTO signupDTO;

    public SignupController(SignupService signupService, UserDataRepository userDataRepository) {
        this.signupService = signupService;
        this.userDataRepository = userDataRepository;
    }

    public void signupUser(Context ctx) {
        signupDTO = ctx.bodyAsClass(SignupDTO.class);
        signupService.getSignupData(signupDTO);
        System.out.println(signupService.validateSignupData());
        System.out.println("User existence status: " + signupService.registerUser());

        if (signupService.isUserRegistered()) {
            ctx.status(200).result("User is successfully created in database!");
        }

        else if (!signupService.isUserRegistered()) {
            ctx.status(400).result("User already exists!");
        }

        else {
            ctx.status(500).result("server error!");
        }
    }
}
