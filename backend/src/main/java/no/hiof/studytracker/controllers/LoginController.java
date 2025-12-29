package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.LoginDTO;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.exceptions.UserAuthenticationException;
import main.java.no.hiof.studytracker.service.LoginService;

import java.util.Map;

public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public void loginUser(Context ctx) {
        LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        try {
            loginService.authenticateUser(email, password);
            String token = loginService.createSessionToken(email, password);
            ctx.status(200).json(Map.of(
                    "messsage", "User is authenticated",
                    "authenticated user", loginDTO.getEmail(),
                    "token", loginService.getSessionTokenId(token)
            ));

        } catch (UserAuthenticationException e) {
            ctx.status(401).json(Map.of(
                    "message", "User is unauthorized",
                    "unauthorized user", loginDTO.getEmail()
            ));
        }

        catch (CustomException e) {
            ctx.status(404).json(Map.of(
                    "message", e.getMessage(),
                    "errorcode", e.getErrorCode()
            ));
        }
    }
}
