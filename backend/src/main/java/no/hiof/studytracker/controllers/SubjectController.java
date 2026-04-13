package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.CreateSubjectRequest;
import no.hiof.studytracker.exceptions.UnauthorizedException;
import no.hiof.studytracker.model.Subject;
import no.hiof.studytracker.service.AuthenticationService;
import no.hiof.studytracker.service.SubjectService;

import java.util.Map;

public class SubjectController {

    private SubjectService subjectService;
    private AuthenticationService authService;

    public SubjectController(SubjectService subjectService, AuthenticationService authService) {
        this.subjectService = subjectService;
        this.authService = authService;
    }

    public void createSubject(Context ctx) {
        try {
            int userId = authService.getUserId(ctx);

            CreateSubjectRequest request = ctx.bodyAsClass(CreateSubjectRequest.class);

            Subject subject = subjectService.createSubject(request.name(), userId);

            ctx.status(201).json(subject);

        } catch (UnauthorizedException e) {
            ctx.status(401).json(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "Something went wrong"));
        }
    }

    public void getSubjects(Context ctx) {
        int userId = authService.getUserId(ctx);
        ctx.json(subjectService.getSubjects(userId));
    }
}