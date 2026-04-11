package no.hiof.studytracker;

import io.javalin.Javalin;
import no.hiof.studytracker.controllers.*;
import no.hiof.studytracker.database.DB;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.*;

import java.sql.Connection;
import java.sql.SQLException;


public class Application {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Kjør migrations ved oppstart
        DB.migrate();
        try (Connection conn = DB.getConnection()) {
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7000"));
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(rule -> rule.anyHost())
            );
        }).start(port);

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // Skriver til Render-logg
            // Send detaljene til Postman så vi ser dem med en gang!
            ctx.status(500).result("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        });

        UserDataRepository userDataRepository = new UserDataRepository();

        // Signup section
        SignupService signupService = new SignupService(userDataRepository);
        SignupController signupController = new SignupController(signupService, userDataRepository);

        // Login section
        LoginService loginService = new LoginService(userDataRepository);
        LoginController loginController = new LoginController(loginService);


        // Session operations section
        SessionService sessionService = new SessionService(userDataRepository);
        SessionController sessionController = new SessionController(sessionService);

        // Session authentication
        AuthenticationService authenticationService = new AuthenticationService(userDataRepository);
        AuthenticationController authenticationController = new AuthenticationController(authenticationService);

        // Dashboard data

        DashboardService dashboardService = new DashboardService(userDataRepository, sessionService);
        DashboardController dashboardController = new DashboardController(dashboardService);

        // Subject
        SubjectService subjectService = new SubjectService(new UserDataRepository());
        SubjectController subjectController = new SubjectController(subjectService, authenticationService);

        app.get("/", ctx -> {
            ctx.result("StudyTracker backend is live 🚀");
        });

        app.post("/auth/signup", ctx -> {
            signupController.signupUser(ctx);
        });

        app.post("/auth/login", ctx -> {
            loginController.loginUser(ctx);
        });

        app.post("/session/session-registration", ctx -> {
            sessionController.studySession(ctx);
        });

        app.get("/session/sessions", ctx -> {
            sessionController.retrieveSessions(ctx);
        });

        app.put("/session/{sessionsId}", ctx -> {
            sessionController.updateSession(ctx);
        });

        app.delete("/session/{sessionId}", ctx -> {
           sessionController.deleteSession(ctx);
        });

        app.get("/auth/validate-session", ctx -> {
            authenticationController.sessionExpiration(ctx);
        });

        app.get("/session/dashboard", ctx -> {
           dashboardController.getDashboard(ctx);
        });

        app.post("api/subjects", subjectController::createSubject);
        app.get("api/subjects", subjectController::getSubjects);
    }
}