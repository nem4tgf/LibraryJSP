package org.example.demo1.utils;

import org.example.demo1.dao.UserDAO;
import org.example.demo1.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateOldPasswords {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        try {
            List<User> users = userDAO.getAllUsers();
            System.out.println("Total users found: " + users.size());
            for (User user : users) {
                String currentPassword = user.getPassword();
                System.out.println("Checking user: " + user.getUsername() + " | Current password: " + currentPassword);

                // Kiểm tra xem mật khẩu đã được băm chưa
                if (!currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$")) {
                    String hashedPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt(12));
                    System.out.println("Updating " + user.getUsername() + ": " + currentPassword + " -> " + hashedPassword);

                    // Cập nhật database
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE users SET password = ? WHERE id = ?")) {
                        stmt.setString(1, hashedPassword);
                        stmt.setInt(2, user.getId());
                        int rowsAffected = stmt.executeUpdate();
                        System.out.println("Rows affected: " + rowsAffected);
                    }
                } else {
                    System.out.println(user.getUsername() + " already hashed: " + currentPassword);
                }
            }
            System.out.println("All passwords updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}