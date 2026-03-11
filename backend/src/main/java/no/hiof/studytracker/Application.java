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
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;


public class Application {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // Kjør migrations ved oppstart
        DB.migrate();
        try (Connection conn = DB.getConnection()) {
            System.out.println("SQLite fungerer!");
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

        System.out.println(loginService.createSessionToken("yahye10@example.com", "12345678"));
        System.out.println(userDataRepository.getSessionTokenIdExpiresAt("7f961352-7366-45b8-8a4d-c4e5bf8e66b8"));
    }
}