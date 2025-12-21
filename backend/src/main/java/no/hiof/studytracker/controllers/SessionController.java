package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.service.SessionService;

import java.util.Map;

public class SessionController {
    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void studySession(Context ctx) {
        try {
            sessionService.studySession(ctx);
            ctx.status(201).json(Map.of(
                    "message: ", "study session is successfully created"
            ));

        } catch (CustomException e) {
            ctx.status(401).json(Map.of(
                    "message: ", e.getMessage(),
                    "errorcode", e.getErrorCode()));
        }
    }
}
