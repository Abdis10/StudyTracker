package main.java.no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.service.SessionService;

import java.util.*;

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
            String token = cxt.header("Authorization").substring(7);
            cxt.status(200).json(sessionService.getSessions(token));
        } catch (CustomException e) {
            cxt.status(401).json(Map.of(
                    "message", e.getErrorCode()
            ));
        }
    }

    public void updateSession(Context cxt) {
        try {
            int sessionId = Integer.parseInt(cxt.pathParam("sessionsId"));
            SessionDataDTO sessionDataDTO = cxt.bodyAsClass(SessionDataDTO.class);
            String token = cxt.header("Authorization").substring(7);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("date", sessionDataDTO.getDate());
            map.put("hours", sessionDataDTO.getHours());
            map.put("productivityScore", sessionDataDTO.getProductivityScore());
            map.put("comment", sessionDataDTO.getComment());

            System.out.println(sessionService.validateUpdateSessionData(map));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
