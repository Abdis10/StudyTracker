package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.controllers.SignupController;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.service.SignupResult;
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
        SignupDTO signupDTO = new SignupDTO("Zak", "Ahmed", "Zak10", "zak12345@example.com","Zak12345", "male");
        UserDataRepository userDataRepository = new UserDataRepository();
        SignupService signupService = new SignupService(userDataRepository);
        SignupController signupController = new SignupController(signupService, userDataRepository);

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
        });

        app.post("/auth/signup", ctx -> {
            signupController.signupUser(ctx);
        });

    }
}