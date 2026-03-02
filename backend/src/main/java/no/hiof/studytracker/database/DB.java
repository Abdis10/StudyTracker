package no.hiof.studytracker.database;

import no.hiof.studytracker.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String RAW_URL =
            System.getenv().getOrDefault(
                    "DATABASE_URL",
                    "jdbc:sqlite:database/studytracker.db"
            );

    private static final String URL = convertUrl(RAW_URL);

    public static void migrate() {
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

            System.out.println("Database migrations completed.");

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database migration failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database connection failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        }
    }

    private static String convertUrl(String url) {

        if (url.startsWith("postgres://")) {

            url = url.replace("postgres://", "");

            String[] parts = url.split("@");

            String userPass = parts[0];
            String hostDb = parts[1];

            String[] userPassSplit = userPass.split(":");
            String username = userPassSplit[0];
            String password = userPassSplit[1];

            return "jdbc:postgresql://" + hostDb +
                    "?user=" + username +
                    "&password=" + password;
        }

        return url;
    }
}