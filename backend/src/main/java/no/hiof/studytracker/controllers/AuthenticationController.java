package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.service.AuthenticationService;

import java.util.Map;

public class AuthenticationController {
    AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void sessionExpiration(Context ctx) {
        try {
            String token = ctx.header("Authorization").substring(7);
            ctx.status(200).json(authenticationService.isSessionValid(token));

        } catch (InvalidTokenException e) {
            ctx.status(400).json(Map.of(
               "message", e.getMessage(),
               "errorCode", e.getErrorCode()
            ));
        }
    }
}
