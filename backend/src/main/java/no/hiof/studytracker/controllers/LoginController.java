package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.LoginDTO;
import no.hiof.studytracker.DTOs.LoginResponseDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.UserAuthenticationException;
import no.hiof.studytracker.service.LoginService;

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
            LoginResponseDTO loginResponseDTO = loginService.createSessionToken(email, password);

            if (loginResponseDTO.firstname() == null || loginResponseDTO.token() == null || loginResponseDTO.email() == null || loginResponseDTO.username() == null) {
                throw new CustomException("User data is missing from the database.", "MISSING_DATA");
            }

            ctx.status(200).json(loginResponseDTO);

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
