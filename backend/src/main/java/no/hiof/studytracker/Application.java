package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.controllers.LoginController;
import main.java.no.hiof.studytracker.controllers.SessionController;
import main.java.no.hiof.studytracker.controllers.SignupController;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.LoginService;
import main.java.no.hiof.studytracker.service.SessionService;
import main.java.no.hiof.studytracker.service.SignupService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Application {

    public static void main(String[] args) throws SQLException {

        // Kjør migrations ved oppstart
        DB.migrate();
        try (Connection conn = DB.getConnection()) {
            System.out.println("SQLite fungerer!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Javalin app = Javalin.create().start(7000);
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
    }
}