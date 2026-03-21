package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.DashboardDTO;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.service.DashboardService;

import java.util.Map;

public class DashboardController {
    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void getDashboard(Context ctx) {
        try {
            String token = ctx.header("Authorization").substring(7);
            DashboardDTO dto = dashboardService.getDashboardData(token);

            ctx.status(200).json(dto);
        } catch (CustomException e) {
            ctx.status(400).json(Map.of(
                    "message", e.getMessage(),
                    "errorCode", e.getErrorCode()
            ));
        }

    }
}
