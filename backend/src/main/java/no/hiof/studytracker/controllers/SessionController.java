package main.java.no.hiof.studytracker.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JsonMapper;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.DTOs.SessionResponseDTO;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.service.SessionService;

import java.util.ArrayList;
import java.util.List;
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
                    "errorcode", e.getErrorCode()
            ));
        }
    }

    public void retrieveSessions(Context cxt) {
        try {
            ArrayList<SessionResponseDTO> allSessions = sessionService.getAllSessions(cxt);
            cxt.status(200).json(allSessions);

        } catch (CustomException e) {
            cxt.status(500).json(Map.of(
               "Message: ", e.getMessage(),
               "Errorcode: ", e.getErrorCode()
            ));
        }
    }
}
