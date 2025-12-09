package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.DTOs.SignupDTO;
import main.java.no.hiof.studytracker.controllers.SignupController;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.model.Article;
import main.java.no.hiof.studytracker.model.ArticleNote;
import main.java.no.hiof.studytracker.model.Note;
import main.java.no.hiof.studytracker.model.User;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.repository.UserRepository;
import main.java.no.hiof.studytracker.service.SignupService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Application {

    public static void main(String[] args) throws SQLException {

        // Kjør migrations ved oppstart
        DB.migrate();
        try (Connection conn = DB.getConnection()) {
            System.out.println("SQLite fungerer!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /*User user1 = new User(1, "Abdullahi", "Ahmed", "Abdulla10", "abdis123@example.com","test12345", "male", "07.12.2025");
        User user2 = new User(2, "Ibrahim", "Ahmed", "Ibra10", "ibra@example.com" ,"ibra12345", "male", "07.12.2025");

        UserDataRepository userDataRepository = new UserDataRepository();
        //userDataRepository.saveUser(user1);
        userDataRepository.saveUser(user2);*/

        Javalin app = Javalin.create().start(7000);
        SignupDTO signupDTO = new SignupDTO("Abdullahi", "Ahmed", "Abdulla07", "abdis12345@example.com","test12345", "male");
        SignupDTO signupDTO1 = new SignupDTO();
        UserDataRepository userDataRepository = new UserDataRepository();
        SignupService signupService = new SignupService(signupDTO1, userDataRepository);
        SignupController signupController = new SignupController(signupService);
        signupController.signupUser(signupDTO);

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
        });

        app.get("/auth/signup", ctx -> {

        });

    }
}