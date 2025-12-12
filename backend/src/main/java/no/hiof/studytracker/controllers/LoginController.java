package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.LoginDTO;
import main.java.no.hiof.studytracker.service.LoginService;

public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /*public boolean loginUser(Context ctx) {
        LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
        String email = loginDTO.getEmail();
        return loginService.authenticateEmail(email);
    }*/

    public String loginUser() {
        return loginService.authenticatePassword();
    }



}
