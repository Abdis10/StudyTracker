package main.java.no.hiof.studytracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private static final String URL = "jdbc:sqlite:database/studytracker.db";

    // Kjør migrations (database‐endringer som er lagret som kode), Kalles kun ved app-oppstart
    public static void migrate() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();

            String sql = """
                CREATE TABLE IF NOT EXISTS sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT NOT NULL,
                    hours INTEGER NOT NULL,
                    productivity INTEGER,
                    comment TEXT
                );
            """;

            stmt.execute(sql);     // migration
            System.out.println("Database Migrations fullført.");
        }
        catch (Exception e) {
            System.err.println("Database Migration-feil: " + e.getMessage());
            throw new RuntimeException("Kunne ikke koble til SQlite");
        }

    }

    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
