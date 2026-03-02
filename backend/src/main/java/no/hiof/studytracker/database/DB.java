package no.hiof.studytracker.database;

import no.hiof.studytracker.exceptions.DatabaseException;

import java.sql.*;

public class DB {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DATABASE_URL",
                    "jdbc:sqlite:database/studytracker.db"
            );

    public static void migrate() {
        try (Connection conn = DriverManager.getConnection(convertUrl(URL))) {
            Statement stmt = conn.createStatement();

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

            stmt.execute(createUsersTable);
            stmt.execute(createSessionsTable);
            stmt.execute(createSessionTokenTable);

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
            return url.replace("postgres://", "jdbc:postgresql://");
        }
        return url;
    }
}