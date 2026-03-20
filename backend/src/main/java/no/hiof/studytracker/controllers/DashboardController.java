package no.hiof.studytracker.controllers;

import io.javalin.http.Context;
import no.hiof.studytracker.DTOs.DashboardDTO;
import no.hiof.studytracker.service.DashboardService;

public class DashboardController {
    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void getDashboard(Context ctx) {
        String token = ctx.header("Authorization").substring(7);
        //DashboardDTO dto = dashboardService.getDashboardData(token);
    }
}
