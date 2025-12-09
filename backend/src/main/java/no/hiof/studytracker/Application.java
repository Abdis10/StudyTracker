package main.java.no.hiof.studytracker;

import io.javalin.Javalin;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.model.Article;
import main.java.no.hiof.studytracker.model.ArticleNote;
import main.java.no.hiof.studytracker.model.Note;
import main.java.no.hiof.studytracker.model.User;
import main.java.no.hiof.studytracker.repository.UserDataRepository;
import main.java.no.hiof.studytracker.repository.UserRepository;

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

        app.get("/", ctx -> {
           ctx.result("Hei fra Javalin!");
        });



    }
}