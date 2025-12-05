package main.java.no.hiof.studytracker;

import main.java.no.hiof.studytracker.database.DB;

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
    }
}