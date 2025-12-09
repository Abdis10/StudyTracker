package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.service.SignupService;

public class SignupController {
    private SignupService signupService;
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    public void signupUser(SignupDTO signupDTO) {
        signupService.getSignupData(signupDTO);
        System.out.println(signupService.validateSignupData());
        System.out.println("User existence status: " + signupService.getUserExists());
    }
}
