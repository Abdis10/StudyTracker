package main.java.no.hiof.studytracker.repository;

import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.exceptions.CustomException;
import main.java.no.hiof.studytracker.model.User;

import java.sql.*;
import java.time.LocalDateTime;

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


}
