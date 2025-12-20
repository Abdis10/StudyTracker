package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.exceptions.StudySessionException;
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
            sessionService.studySession(ctx);
            ctx.status(200).json(Map.of(
                    "message: ", "study session is successfully created"
            ));

        } catch (StudySessionException e) {
            ctx.status(401).json(Map.of(
                    "message: ", e.getMessage()));
        }
    }
}
