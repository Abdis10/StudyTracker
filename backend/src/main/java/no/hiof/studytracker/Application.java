package main.java.no.hiof.studytracker;

import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.model.Article;
import main.java.no.hiof.studytracker.model.ArticleNote;
import main.java.no.hiof.studytracker.model.Note;

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

        ArticleNote articleNote = new ArticleNote(1, 2, "Spring Boot tutorial", "nothing so far", "06.12.2025");

        Article article = new Article(1, 2, "Spring Boot tutorial", "nothing so far", "06.12.2025");
        Note note = new Note(1, 3, "Differensialligninger", "nothing so far", "05.12.2025");

        System.out.println(article.toString());
        System.out.println(note.toString());
    }
}