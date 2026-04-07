package no.hiof.studytracker.database;

import io.github.cdimascio.dotenv.Dotenv;
import no.hiof.studytracker.exceptions.DatabaseException;

import java.sql.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DB {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env.local").ignoreIfMissing().load();

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

    public static void migrate() throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String createMigrationSchema = """
                    CREATE TABLE IF NOT EXISTS schema_migrations (
                        version TEXT PRIMARY KEY,
                        applied_at TIMESTAMP NOT NULL
                    );
                    """;

            conn.createStatement().execute(createMigrationSchema);

            List<Migration> migrations = List.of(
                    new Migration("V001_create_user_profile", """
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
                            """),
                    new Migration("V002_create_sessions", """
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
                            """),
                    new Migration("V003_create_session_token", """
                            CREATE TABLE IF NOT EXISTS session_token (
                                session_token_id TEXT PRIMARY KEY,
                                user_id INTEGER NOT NULL REFERENCES user_profile(id),
                                created_at TIMESTAMP,
                                expires_at TIMESTAMP
                            );
                            """),
                    new Migration("V004_create_subject", """
                            CREATE TABLE IF NOT EXISTS subjects (
                                id SERIAL PRIMARY KEY,
                                user_id INTEGER NOT NULL REFERENCES user_profile(id)
                            );
                            """),
                    new Migration("V004_add_subject_name_to_subjects", """
                            ALTER TABLE subjects
                            ADD subject_name TEXT
                            """),
                    new Migration("V005_add_subject_id_to_sessions", """
                            ALTER TABLE sessions 
                            ADD COLUMN subject_id INTEGER;
                        
                            ALTER TABLE sessions
                            ADD CONSTRAINT fk_sessions_subject
                            FOREIGN KEY (subject_id) 
                            REFERENCES subjects(id)
                            ON DELETE SET NULL;
                        """)
            );


            Set<String> applied = getAppliedMigrations(conn);

            for (Migration m : migrations) {
                if(!applied.contains(m.getVersion())) {
                    System.out.println("Running migration: " + m.getVersion());

                    conn.createStatement().execute(m.getSql());

                    String sql = """
                        INSERT INTO schema_migrations (version, applied_at)
                        VALUES(?, NOW());
                    """;

                    PreparedStatement pstm = conn.prepareStatement(sql);
                    pstm.setString(1, m.getVersion());
                    pstm.executeUpdate();

                }
            }

            conn.commit();
            System.out.println("Database migrations completed successfully.");
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new DatabaseException(
                    "Database migration failed: " + e.getMessage(),
                    "DB-MIGRATION-FAILED",
                    e
            );
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new SQLException("Couldn't close database connection", e);
            }
        }
    }

    public static Connection getConnection() {
        try {
            // if dotenv.get("DATABASE_URL") == null is true, then it means we are in local dev
            if (URL != null && !URL.isEmpty() && dotenv.get("DATABASE_URL") == null) {
                // PRODUCTION
                return DriverManager.getConnection(URL);
            } else {
                // LOCAL DEV
                if (URL == null || USER == null || PASSWORD == null) {
                    System.out.println("URL:" + (URL != null ? "SET": "MISSING"));
                    System.out.println("USER: " + (USER != null ? "SET" : "MISSING"));
                    System.out.println("PASSWORD: " +  (PASSWORD != null ? "SET" : "MISSING"));
                    throw new DatabaseException(
                            "Missing DATABASE_URL, DATABASE_USER or DATABASE_PASSWORD",
                            "DB-CONFIG-ERROR"
                    );
                } else {
                    return DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(
                    "Database connection failed",
                    "DB-CONNECTION-FAILED",
                    e
            );
        }
    }

    public static Set<String> getAppliedMigrations(Connection conn) throws SQLException {
        String sql = """
                SELECT version FROM schema_migrations;
            """;
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            Set<String> applied = new HashSet<>();
            while(rs.next()) {
                applied.add(rs.getString("version"));
            }

            return applied;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}