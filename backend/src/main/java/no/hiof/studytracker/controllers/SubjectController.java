package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.CreateSubjectRequest;
import no.hiof.studytracker.model.Subject;
import no.hiof.studytracker.service.AuthenticationService;
import no.hiof.studytracker.service.SubjectService;

public class SubjectController {

    private SubjectService subjectService;
    private AuthenticationService authService;

    public SubjectController(SubjectService subjectService, AuthenticationService authService) {
        this.subjectService = subjectService;
        this.authService = authService;
    }

    public void createSubject(Context ctx) {
        int userId = authService.getUserId(ctx);

        CreateSubjectRequest request = ctx.bodyAsClass(CreateSubjectRequest.class);

        Subject subject = subjectService.createSubject(request.name(), userId);

        ctx.json(subject);
    }

    public void getSubjects(Context ctx) {
        int userId = authService.getUserId(ctx);
        ctx.json(subjectService.getSubjects(userId));
    }
}