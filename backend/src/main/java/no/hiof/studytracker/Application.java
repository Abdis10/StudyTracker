package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.controllers.LoginController;
import main.java.no.hiof.studytracker.controllers.SignupController;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.LoginService;
import main.java.no.hiof.studytracker.service.PasswordUtil;
import main.java.no.hiof.studytracker.service.SignupService;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;


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


        String createdAt = LocalDateTime.now().toString();

        StringBuilder expiresAt = new StringBuilder();
        System.out.println("Created at " + createdAt);
        for (int i = 0; i < createdAt.length(); i++) {

            if (i == 9 && ( Integer.parseInt(String.valueOf(createdAt.charAt(9))) < 9) ) {
                int characterAtIndex9 = Integer.parseInt(String.valueOf(createdAt.charAt(i))) + 1;
                System.out.println(characterAtIndex9);
                expiresAt.append(characterAtIndex9);
                break;
            }

            if ( i == 9 && ( Integer.parseInt(String.valueOf(createdAt.charAt(8))) == 0 &&
                    Integer.parseInt(String.valueOf(createdAt.charAt(9))) == 9) ) {
                int characterAtIndex8 = Integer.parseInt(String.valueOf(createdAt.charAt(8))) + 1; //
                int characterAtIndex9 = Integer.parseInt(String.valueOf(createdAt.charAt(9))) - 9;
                int updateCharacterAtIndex8And9 = characterAtIndex8 + characterAtIndex9;
                expiresAt.append(updateCharacterAtIndex8And9);
            }

            expiresAt.append(createdAt.charAt(i));
        }
        System.out.println("Expires at " + expiresAt);

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
        });

        app.post("/auth/signup", ctx -> {
            signupController.signupUser(ctx);
        });

        app.get("/auth/login", ctx -> {
            loginController.loginUser(ctx);
        });
    }
}