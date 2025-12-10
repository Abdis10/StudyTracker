package main.java.no.hiof.studytracker.repository;

import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.model.User;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDataRepository implements UserRepository {
    public UserDataRepository() {}


    public boolean usernameExists(String username) {
        boolean usernameExists = false;
        try (Connection connection = DB.getConnection()) {
            String sql = "SELECT * FROM user_profile";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while ( rs.next() ) {
                if (rs.getString("username").equalsIgnoreCase(username)){
                    usernameExists = true;
                }

                else {
                    usernameExists = false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return usernameExists;
    }

    public boolean emailExists(String email) {
        boolean emailExists = false;
        try (Connection connection = DB.getConnection()) {
            String sql = "SELECT * FROM user_profile";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while ( rs.next() ) {
                if (rs.getString("email").equalsIgnoreCase(email)){
                    emailExists = true;
                }

                else {
                    emailExists = false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return emailExists;
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
            throw new RuntimeException(e);
        }
    }


}
