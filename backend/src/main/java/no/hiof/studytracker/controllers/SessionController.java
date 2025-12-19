package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.SessionService;

import java.util.Map;

public class SessionController {
    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void studySession(Context ctx) {
        try {
            sessionService.validateSessionData(ctx);
            sessionService.createStudySession(ctx);
            ctx.status(200).json(Map.of(
                    "message: ", "study session is successfully created"
            ));

        } catch (Exception e) {
            ctx.status(401).json(Map.of(
                    "message: ", "error when registring study session"));
        }
    }
}
