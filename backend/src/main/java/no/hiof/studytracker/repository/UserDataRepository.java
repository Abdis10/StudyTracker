package no.hiof.studytracker.repository;

import no.hiof.studytracker.DTOs.SessionResponseDTO;
import no.hiof.studytracker.DTOs.UpdateSessionDTO;
import no.hiof.studytracker.database.DB;
import no.hiof.studytracker.exceptions.CustomException;
import no.hiof.studytracker.model.Session;
import no.hiof.studytracker.model.User;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            pstm.setObject(3,createdAt.toInstant().toString());
            pstm.setObject(4, expiresAt.toInstant().toString());

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
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                return rs.getInt("user_id");
            }
        }

        catch (Exception e) {
            throw new CustomException("Didn't find user id for the token:" + token);
        }

        return Integer.parseInt(null);
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
            pstm.setString(6, session.getCreatedAt());

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
                        rs.getString("created_At"), rs.getString("updated_at"));

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

            ResultSet rs = pstm.executeQuery();
            return rs.getInt("user_id");

        } catch (SQLException e) {
            throw new CustomException("Didn't find user-id that matches given session-id", e);
        }
    }

    public UpdateSessionDTO getSessionBySessionId(int sessionId) {
        String sql = "SELECT date, hours, productivity_score, comment, created_at FROM sessions WHERE id = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, sessionId);

            ResultSet rs = pstm.executeQuery();
            UpdateSessionDTO sessionDataDTO = new UpdateSessionDTO(rs.getString("date"), rs.getFloat("hours"),
                    rs.getInt("productivity_score"), rs.getString("comment"), rs.getString("created_at"));

            return sessionDataDTO;

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
            pstm.setString(5, updateSessionDTO.getUpdatedAt());
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
            while (rs.next()) {
                String dateString = rs.getString("expires_at");
                Timestamp expiresAt = Timestamp.from(Instant.parse(dateString));
                return expiresAt;
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
}
