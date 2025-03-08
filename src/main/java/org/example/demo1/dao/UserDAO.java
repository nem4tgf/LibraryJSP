package org.example.demo1.dao;

import org.example.demo1.model.User;
import org.example.demo1.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static final String SELECT_BY_USERNAME = "SELECT id, username, password, full_name, email, role, created_at FROM users WHERE username = ?";
    private static final String SELECT_BY_ID = "SELECT id, username, password, full_name, email, role, created_at FROM users WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, username, password, full_name, email, role, created_at FROM users";
    private static final String INSERT = "INSERT INTO users (username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_WITH_PASSWORD = "UPDATE users SET password = ?, full_name = ?, email = ?, role = ? WHERE id = ?";
    private static final String UPDATE_WITHOUT_PASSWORD = "UPDATE users SET full_name = ?, email = ?, role = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";

    public User getUserByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        }
    }

    public User getUserById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        }
    }

    public void addUser(User user) throws SQLException {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User data is incomplete");
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.executeUpdate();
            LOGGER.info("User added: " + user.getUsername());
        }
    }

    public void updateUser(User user, String newPassword) throws SQLException {
        if (user == null || user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user data");
        }

        String sql = newPassword != null && !newPassword.isEmpty() ? UPDATE_WITH_PASSWORD : UPDATE_WITHOUT_PASSWORD;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (newPassword != null && !newPassword.isEmpty()) {
                stmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
                stmt.setString(2, user.getFullName());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getRole());
                stmt.setInt(5, user.getId());
            } else {
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getRole());
                stmt.setInt(4, user.getId());
            }
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No user found with id: " + user.getId());
            LOGGER.info("User updated: " + user.getId());
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        }
    }

    public void deleteUser(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No user found with id: " + userId);
            LOGGER.info("User deleted: " + userId);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    }
}