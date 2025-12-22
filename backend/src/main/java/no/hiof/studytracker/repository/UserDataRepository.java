package main.java.no.hiof.studytracker.repository;

import main.java.no.hiof.studytracker.DTOs.SessionDataDTO;
import main.java.no.hiof.studytracker.DTOs.SessionResponseDTO;
import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.model.Session;
import main.java.no.hiof.studytracker.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
        try (Connection connection = DB.getConnection()) {
            String userData = "INSERT INTO user_profile(first_name, last_name, username, email, password_hash, gender, created_at) VALUES(?, ? , ?, ?, ?, ?, ?)";

            String createdAt = LocalDateTime.now().toString();

            PreparedStatement pstm = connection.prepareStatement(userData);

            pstm.setString(1, user.getFirstname());
            pstm.setString(2, user.getLastname());
            pstm.setString(3, user.getUsername());
            pstm.setString(4, user.getEmail());
            pstm.setString(5, user.getPassword());
            pstm.setString(6, user.getGender());
            pstm.setString(7, createdAt);

            pstm.executeUpdate();
        } catch (Exception e) {
            throw new CustomException("Couldn't save user in database", e);
        }
    }

    public void saveSessionToken(String sessionTokenId, int userId, String createdAt, String expiresAt) {
        String sessionData = "INSERT INTO session_token(session_token_id, user_id, created_at, expires_at) VALUES(?, ?, ?, ?)";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sessionData);

            pstm.setString(1, sessionTokenId);
            pstm.setInt(2, userId);
            pstm.setString(3,createdAt);
            pstm.setString(4, expiresAt);

            pstm.executeUpdate();
        } catch (Exception e) {
            throw new CustomException("Couldn't save session token in database", e);
        }
    }

    public String getId(String email) {
        String sql = "SELECT id, 1 from user_profile WHERE email = ?";

        try (Connection connection = DB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getString("id");
            }

        } catch (Exception e) {
            throw new CustomException("User id doesn't exist!", e);
        }

        return null;
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

    public int getIdByTokenId(String token) {
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

        return 0;
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

    public ArrayList<SessionResponseDTO> getSessions(int userId) {
        String sql = "SELECT date, hours, productivity_score, comment, created_at, updated_at FROM sessions WHERE user_id = ?";

        try (Connection connection = DB.getConnection()) {
            ArrayList<SessionResponseDTO> arrayOfSessions = new ArrayList<>();

            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, userId);


            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                SessionResponseDTO sessionResponseDTO = new SessionResponseDTO(rs.getString("date"), rs.getFloat("hours"),
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

}
