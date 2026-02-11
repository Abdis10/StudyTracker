package no.hiof.studytracker;

import io.javalin.Javalin;
import no.hiof.studytracker.controllers.AuthenticationController;
import no.hiof.studytracker.controllers.LoginController;
import no.hiof.studytracker.controllers.SessionController;
import no.hiof.studytracker.controllers.SignupController;
import no.hiof.studytracker.database.DB;
import no.hiof.studytracker.repository.UserDataRepository;
import no.hiof.studytracker.service.AuthenticationService;
import no.hiof.studytracker.service.LoginService;
import no.hiof.studytracker.service.SessionService;
import no.hiof.studytracker.service.SignupService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;


public class Application {

    public static void main(String[] args) throws SQLException {

        // Kjør migrations ved oppstart
        DB.migrate();
        try (Connection conn = DB.getConnection()) {
            System.out.println("SQLite fungerer!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(rule -> rule.anyHost())
            );
        }).start(7000);
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

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
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

        app.get("/authenticateSession", ctx -> {
            authenticationController.sessionExpiration(ctx);
        });
    }
}