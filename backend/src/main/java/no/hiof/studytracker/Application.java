package main.java.no.hiof.studytracker;

import no.hiof.studytracker.database.DB;

import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        try (var conn = DB.connect()) {
            System.out.println("SQLite fungerer!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}