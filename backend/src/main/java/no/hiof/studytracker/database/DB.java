package no.hiof.studytracker.database;

import io.github.cdimascio.dotenv.Dotenv;
import no.hiof.studytracker.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing().load();

    public static String getEnvOrDotenv(String key) {
        // brukes av render for å hente env
        String value = System.getenv(key);

        if (value != null && !value.isEmpty()) {
            return value;
        }

        // fallback for local dev
        return dotenv.get(key);
    }

    private static final String URL = getEnvOrDotenv("DATABASE_URL");
    private static final String USER = getEnvOrDotenv("DATABASE_USER");
    private static final String PASSWORD = getEnvOrDotenv("DATABASE_PASSWORD");

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
        System.out.println("Connecting to database...");

        try {
            if (URL != null && !URL.isEmpty()) {
                // PRODUCTION
                return DriverManager.getConnection(URL);
            } else {
                // LOCAL DEV
                validateEnv();
                return DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database connection failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        }
    }

    private static void validateEnv() {
        if (URL == null || USER == null || PASSWORD == null) {
            System.out.println("URL:" + (URL != null ? "SET": "MISSING"));
            System.out.println("USER: " + (USER != null ? "SET" : "MISSING"));
            System.out.println("PASSWORD: " +  (PASSWORD != null ? "SET" : "MISSING"));
            throw new DatabaseException(
                    "Missing DATABASE_URL, DATABASE_USER or DATABASE_PASSWORD",
                    "DB-CONFIG-ERROR"
            );
        }
    }
}