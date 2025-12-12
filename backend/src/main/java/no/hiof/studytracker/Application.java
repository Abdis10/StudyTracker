package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.controllers.LoginController;
import main.java.no.hiof.studytracker.controllers.SignupController;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.LoginService;
import main.java.no.hiof.studytracker.service.PasswordUtil;
import main.java.no.hiof.studytracker.service.SignupService;
import java.sql.Connection;
import java.sql.SQLException;


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
        // System.out.println(loginController.loginUser("Yasin123"));


        System.out.println();
        System.out.println(loginController.loginUser());

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
        });

        app.post("/auth/signup", ctx -> {
            signupController.signupUser(ctx);
        });

        /*app.get("/auth/login", ctx -> {
            loginController.loginUser(ctx);
        });*/
    }
}