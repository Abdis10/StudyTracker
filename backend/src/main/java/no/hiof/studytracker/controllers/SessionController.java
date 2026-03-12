package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.SessionDataDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.exceptions.InvalidTokenException;
import no.hiof.studytracker.exceptions.SessionOwnershipException;
import no.hiof.studytracker.service.SessionService;

import java.util.*;

public class SessionController {
    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void studySession(Context ctx) {
        try {
            String token = ctx.header("Authorization").substring(7);
            SessionDataDTO sessionDataDTO = ctx.bodyAsClass(SessionDataDTO.class);
            sessionDataDTO.setToken(token);
            sessionService.studySession(sessionDataDTO);
            ctx.status(201).json(Map.of(
                    "message", "study session is successfully created"
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
            UpdateSessionDTO updateSessionDTO = cxt.bodyAsClass(UpdateSessionDTO.class);
            String token = cxt.header("Authorization").substring(7);
            sessionService.updateSessionInRepo(updateSessionDTO, token, sessionId);
            cxt.status(200).json(Map.of(
                    "Message: ", "Session successfully updated"
            ));
        } catch (SessionOwnershipException e) {
            cxt.status(401).json(Map.of(
               "Message: ", e.getErrorCode()
            ));
        } catch (InvalidTokenException e) {
            cxt.status(403).json(Map.of(
                    "Message: ", e.getErrorCode()
            ));
        } catch (CustomException e) {
            cxt.status(404).json(Map.of(
                    "Message: ", e.getErrorCode()
            ));
        }
    }


    public void deleteSession(Context ctx) {
        try {
            int sessionId = Integer.parseInt(ctx.pathParam("sessionId"));
            String token = ctx.header("Authorization").substring(7);
            sessionService.deleteSessionForUser(token, sessionId);
            ctx.status(204);
        } catch (InvalidTokenException e) {
            ctx.status(401).json(Map.of(
                    "Message", e.getErrorCode()
            ));
        } catch (SessionOwnershipException e) {
            ctx.status(403).json(Map.of(
                    "Message", e.getErrorCode()
            ));
        } catch (CustomException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getErrorCode());
            response.put("message", e.getMessage()); // e.getMessage() er standard fra Exception
            ctx.status(404).json(response);
        }
    }

}
