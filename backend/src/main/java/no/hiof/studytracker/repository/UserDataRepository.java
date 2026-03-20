package no.hiof.studytracker.repository;

import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.database.DB;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.model.User;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserDataRepository implements UserRepository {
    public UserDataRepository() {}

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM user_profile WHERE username = ? LIMIT 1";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returnerer true hvis en rad finnes

        } catch (SQLException e) {
            throw new CustomException("Database error while checking username", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM user_profile WHERE email = ? LIMIT 1";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            throw new CustomException("Database error while checking email", e);
        }
    }


    public String getPasswordHash(String email) {
        String sql = "SELECT 1, password_hash FROM user_profile WHERE email = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getString("password_hash");
            }

        } catch (Exception e) {
            throw new CustomException("Database error, couldn't find email");
        }

        return null;
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO user_profile(first_name, last_name, username, email, password_hash, gender, created_at) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, user.getFirstname());
            pstm.setString(2, user.getLastname());
            pstm.setString(3, user.getUsername());
            pstm.setString(4, user.getEmail());
            pstm.setString(5, user.getPassword());
            pstm.setString(6, user.getGender());

            // FIKS: Bruker java.sql.Timestamp i stedet for String
            pstm.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            pstm.executeUpdate();

        } catch (SQLException e) {
            // Logg den faktiske SQL-feilen så du ser den i Render
            throw new CustomException("Couldn't save user in database: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomException("Unexpected error during save", e);
        }
    }

    public void saveSessionToken(String sessionTokenId, int userId, Timestamp createdAt, Timestamp expiresAt) {
        String sessionData = "INSERT INTO session_token(session_token_id, user_id, created_at, expires_at) VALUES(?, ?, ?, ?)";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sessionData);

            pstm.setString(1, sessionTokenId);
            pstm.setInt(2, userId);
            pstm.setTimestamp(3, createdAt);
            pstm.setTimestamp(4, expiresAt);

            pstm.executeUpdate();
        } catch (Exception e) {
            throw new CustomException("Couldn't save session token in database", e);
        }
    }

    public int getId(String email) {
        String sql = "SELECT id, 1 from user_profile WHERE email = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            throw new CustomException("User id doesn't exist!", e);
        }

        return 0;
    }


    public String sessionTokenId(int userID) {
        String sql = "SELECT session_token_id FROM session_token WHERE user_id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userID);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                return rs.getString("session_token_id");
            }

        } catch (Exception e) {
            throw new CustomException("Couldn't find any session token for user with id " + userID);
        }

        return null;
    }

    public int getUserIdByToken(String token) {
        String sql = "SELECT user_id FROM session_token WHERE session_token_id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, token);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }

        catch (SQLException e) {
            // Logg den faktiske databasefeilen for din egen del
            throw new CustomException("Database error during token lookup", "DB_ERROR");
        }

        // Hvis vi kommer hit, betyr det at rs.next() var false (token finnes ikke)
        throw new CustomException("No user found for the provided token", "INVALID_TOKEN");
    }

    public void registerStudySession(Session session) {
        String sql = "INSERT INTO sessions(user_id, date, hours, productivity_score, comment, created_at) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setInt(1, session.getUserId());
            pstm.setString(2, session.getDate());
            pstm.setFloat(3, session.getHours());
            pstm.setInt(4, session.getProductivityScore());
            pstm.setString(5, session.getComment());
            pstm.setTimestamp(6, session.getCreatedAt());

            pstm.executeUpdate();

        } catch (Exception e) {
            throw new CustomException("Couldn't save session in database");
        }

    }

    public boolean doesTokenExist(String token) {
        String sql = "SELECT session_token_id FROM session_token WHERE session_token_id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, token);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        }

        catch (SQLException e) {
            throw new CustomException("Unidentified token");
        }
    }

    public List<SessionResponseDTO> getSessions(int userId) {
        String sql = "SELECT id, date, hours, productivity_score, comment, created_at, updated_at FROM sessions WHERE user_id = ?";

        try (Connection connection = DB.getConnection()) {
            ArrayList<SessionResponseDTO> arrayOfSessions = new ArrayList<>();

            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userId);


            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                SessionResponseDTO sessionResponseDTO = new SessionResponseDTO(rs.getInt("id"), rs.getString("date"), rs.getFloat("hours"),
                        rs.getInt("productivity_score"), rs.getString("comment"),
                        rs.getTimestamp("created_At"), rs.getTimestamp("updated_at"));

                arrayOfSessions.add(sessionResponseDTO);
            }

            return arrayOfSessions;
        }

        catch (SQLException e) {
            throw new CustomException("Database error!", e.getCause());
        }

    }

    public int getUserIdBySessionId(int sessionId) {
        String sql = "SELECT user_id FROM sessions WHERE id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, sessionId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) { // Flytt markøren til første rad
                    return rs.getInt("user_id");
                } else {
                    // Her håndterer vi at session_id ikke fantes i databasen
                    throw new CustomException("No session found with ID: " + sessionId, "SESSION_NOT_FOUND");
                }
            }

        } catch (SQLException e) {
            throw new CustomException("Database error while fetching user ID", "DB_ERROR");
        }
    }

    public UpdateSessionDTO getSessionBySessionId(int sessionId) {
        String sql = "SELECT date, hours, productivity_score, comment, created_at FROM sessions WHERE id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, sessionId);

            try (ResultSet rs = pstm.executeQuery()) {
                // sjekke om det finnes en rad før vi henter data
                if (rs.next()) {
                    return new UpdateSessionDTO(
                            rs.getString("date"),
                            rs.getFloat("hours"),
                            rs.getInt("productivity_score"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at")
                    );
                } else {
                    // Hvis vi kommer hit, fantes ikke sessionId i databasen
                    throw new CustomException("Session with ID " + sessionId + " not found", "NOT_FOUND");
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error in database when retrieving session by session-id", e);
        }
    }


    public int updateSession(int sessionId, UpdateSessionDTO updateSessionDTO) {
        String sql = "UPDATE sessions " +
                "SET date = ?, hours = ?, productivity_score = ?, comment = ?, updated_at = ? " +
                "WHERE id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, updateSessionDTO.getDate());
            pstm.setFloat(2, updateSessionDTO.getHours());
            pstm.setInt(3, updateSessionDTO.getProductivityScore());
            pstm.setString(4, updateSessionDTO.getComment());
            pstm.setTimestamp(5, updateSessionDTO.getUpdatedAt());
            pstm.setInt(6, sessionId);

            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error when updating session in DB", e);
        }
    }

    public int deleteSession(int sessionId) {
        String sql = "DELETE FROM sessions WHERE id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, sessionId);

            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error when deleting session in DB", e);
        }

    }

    public Timestamp getSessionTokenIdExpiresAt(String token) {
        String sql = "SELECT expires_at FROM session_token WHERE session_token_id = ? ";

        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, token);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("expires_at");
            }

        } catch (SQLException e) {
            throw new CustomException("Error when getting sessionTokenId his expires_at ", e);
        }
        return null;
    }

    public String getUsernameByUserid(int id) {
        String sql = "SELECT username FROM user_profile WHERE id = ?";
        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            while (rs.next())
                return rs.getString("username");

        } catch (SQLException e) {
            throw new CustomException("Error when getting username by id", e);
        }
        return null;

    }

    public String getEmailByUserId(int id) {
        String sql = "SELECT email FROM user_profile WHERE id = ?";
        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            while (rs.next())
                return rs.getString("email");

        } catch (SQLException e) {
            throw new CustomException("Error when getting email by id", e);
        }
        return null;

    }


    public String getUserFirstname(int id) {
        String sql = "SELECT first_name FROM user_profile WHERE id = ?";
        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            while (rs.next())
                return rs.getString("first_name");

        } catch (SQLException e) {
            throw new CustomException("Error when getting user's firstname by id", e);
        }
        return null;
    }

    public float getTodaysStudyHours(int userId) {
        String sql = "SELECT SUM(hours) as total FROM sessions WHERE date = ? AND user_id = ?";

        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            LocalDate date = LocalDate.now();

            pstm.setString(1, String.valueOf(date));
            pstm.setInt(2, userId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("total");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public float getWeekStudyHours(int userId) {
        String sql = "SELECT SUM(hours) as total FROM sessions WHERE user_id = ? AND date BETWEEN ? AND ?";

        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userId);

            LocalDate today = LocalDate.now();
            int currentWeekNumber = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int year = today.get(IsoFields.WEEK_BASED_YEAR);

            LocalDate startOfTheWeek = LocalDate.of(year, 1, 4)
                    .with(WeekFields.ISO.weekOfYear(), currentWeekNumber)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            LocalDate endOfTheWeek = startOfTheWeek.plusDays(6);

            pstm.setString(2, startOfTheWeek.toString());
            pstm.setString(3, endOfTheWeek.toString());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    float result = rs.getFloat("total");
                    if (rs.wasNull()) {
                        throw new CustomException("Total study hours of the week is 0", "NO_STUDY_HOURS_REGISTERED_FOR_THE_WEEK");
                    } else {
                        return result;
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return 0;
    }


    public float getMonthStudyHours(int userId) {
        String sql = "SELECT SUM(hours) as total FROM sessions WHERE user_id = ? AND date >= ? AND date < ?";

        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userId);

            LocalDate today = LocalDate.now();
            // Første dag i måneden (f.eks. 2026-03-01)
            LocalDate startOfMonth = today.withDayOfMonth(1);

            // Første dag i NESTE måned (f.eks. 2026-04-01)
            LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

            pstm.setString(2, startOfMonth.toString());
            pstm.setString(3, startOfNextMonth.toString());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    float result = rs.getFloat("total");
                    if (rs.wasNull()) {
                        throw new CustomException("Total study hours of the month is 0", "NO_STUDY_HOURS_REGISTERED_FOR_THE_WEEK");
                    } else {
                        return result;
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0;
    }


    public float getLastWeekStudyHours(int userId) {
        String sql = "SELECT SUM(hours) as total FROM sessions WHERE user_id = ? AND date BETWEEN ? AND ?";

        try(Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userId);

            LocalDate today = LocalDate.now();
            int currentWeekNumber = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            currentWeekNumber = currentWeekNumber - 1; // -1 fordi vi ønsker uken før nåværende uke
            int year = today.get(IsoFields.WEEK_BASED_YEAR);

            LocalDate startOfTheWeek = LocalDate.of(year, 1, 4)
                    .with(WeekFields.ISO.weekOfYear(), currentWeekNumber)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            LocalDate endOfTheWeek = startOfTheWeek.plusDays(6);

            pstm.setString(2, startOfTheWeek.toString());
            pstm.setString(3, endOfTheWeek.toString());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    float result = rs.getFloat("total");
                    if (rs.wasNull()) {
                        throw new CustomException("Total study hours of the week is 0", "NO_STUDY_HOURS_REGISTERED_FOR_THE_WEEK");
                    } else {
                        return result;
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return 0;
    }
}