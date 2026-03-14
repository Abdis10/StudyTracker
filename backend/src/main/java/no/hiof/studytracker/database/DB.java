package no.hiof.studytracker.database;

import no.hiof.studytracker.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL = System.getenv("DATABASE_URL") != null
            ? System.getenv("DATABASE_URL")
            : "jdbc:postgresql://localhost:5432/studytracker";

    private static final String USER = System.getenv("DATABASE_USER") != null
            ? System.getenv("DATABASE_USER")
            : "postgres";

    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD") != null
            ? System.getenv("DATABASE_PASSWORD")
            : "postgres";

    public static void migrate() {
        try (Connection conn = getConnection()) {

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

            String createSessionsTable = """
                CREATE TABLE IF NOT EXISTS sessions (
                    id SERIAL PRIMARY KEY,
                    user_id INTEGER NOT NULL REFERENCES user_profile(id),
                    date TEXT NOT NULL,
                    hours REAL NOT NULL,
                    productivity_score INTEGER,
                    comment TEXT,
                    created_at TIMESTAMP NOT NULL,
                    updated_at TIMESTAMP
                );
            """;

            String createSessionTokenTable = """
                CREATE TABLE IF NOT EXISTS session_token (
                    session_token_id TEXT PRIMARY KEY,
                    user_id INTEGER NOT NULL REFERENCES user_profile(id),
                    created_at TIMESTAMP,
                    expires_at TIMESTAMP
                );
            """;

            conn.createStatement().execute(createUsersTable);
            conn.createStatement().execute(createSessionsTable);
            conn.createStatement().execute(createSessionTokenTable);

            System.out.println("Database migrations completed successfully.");
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
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database connection failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        }
    }
}