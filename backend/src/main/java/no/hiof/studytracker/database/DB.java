package no.hiof.studytracker.database;

import no.hiof.studytracker.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    // Vi trenger bare én variabel for URL
    private static final String URL = System.getenv().getOrDefault(
            "DATABASE_URL",
            "jdbc:sqlite:database/studytracker.db" // Lokal fallback
    );

    public static void migrate() {
        // Fjernet Class.forName - JDBC fikser dette selv
        try (Connection conn = DriverManager.getConnection(URL)) {

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS user_profile (
                    id SERIAL PRIMARY KEY,
                    first_name TEXT,
                    last_name TEXT,
                    username TEXT,
                    email TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    gender TEXT,
                    created_at TIMESTAMP NOT NULL,
                    updated_at TIMESTAMP
                );
            """;

            // ... resten av tabellene dine ...

            conn.createStatement().execute(createUsersTable);
            // Husk å legge til execute for de andre tabellene her også

            System.out.println("Database migrations completed successfully on: " + URL.split(":")[1]);

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database migration failed: " + e.getMessage(),
                    "DB-MIGRATION-FAILED",
                    e
            );
        }
    }

    public static Connection getConnection() {
        try {
            if (URL.contains("postgresql")) {
                Class.forName("org.postgresql.Driver");
            }
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database connection failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}