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

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS user_profile (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT,
                    last_name TEXT,
                    username TEXT,
                    email TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    gender TEXT,
                    created_at TEXT NOT NULL,
                    updated_at TEXT
                );
            """;

            String createSessionsTable = """
                CREATE TABLE IF NOT EXISTS sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    hours REAL NOT NULL,
                    productivity_score INTEGER,
                    comment TEXT,
                    created_at TEXT NOT NULL,
                    updated_at TEXT,
                    FOREIGN KEY (user_id) REFERENCES user_profile(id)
                );
            """;

            stmt.execute(createUsersTable);
            stmt.execute(createSessionsTable);     // migration
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
