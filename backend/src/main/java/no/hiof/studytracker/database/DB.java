package no.hiof.studytracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:sqlite:database/studytracker.db";

    public static Connection connect() throws SQLException {
        try {
            return DriverManager.getConnection(URL);
        }
        catch (Exception e) {
            throw new RuntimeException("Kunne ikke koble til SQlite", e);
        }



    }
}
