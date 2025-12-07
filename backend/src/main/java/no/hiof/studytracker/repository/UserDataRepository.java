package main.java.no.hiof.studytracker.repository;

import main.java.no.hiof.studytracker.database.DB;
import main.java.no.hiof.studytracker.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDataRepository implements UserRepository {
    public UserDataRepository() {}

    public void saveUser(User user) {
        try (Connection connection = DB.getConnection()) {
            String userData = "INSERT INTO user_profile(first_name, last_name, username, email, password_hash, gender, created_at) VALUES(?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstm = connection.prepareStatement(userData)) {
                pstm.setString(1, user.getFirstname());
                pstm.setString(2, user.getLastname());
                pstm.setString(3, user.getUsername());
                pstm.setString(4, user.getEmail());
                pstm.setString(5, user.getPassword());
                pstm.setString(6, user.getGender());
                pstm.setString(7, user.getCreatedAt());

                pstm.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
