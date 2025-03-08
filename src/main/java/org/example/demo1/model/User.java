package org.example.demo1.model;

import java.time.LocalDateTime;

public class User {
    private int id; // Thêm ID
    private String username;
    private String password;
    private String fullName; // Thêm tên đầy đủ
    private String email; // Thêm email
    private String role; // "librarian" hoặc "student"
    private LocalDateTime createdAt; // Thêm thời gian tạo

    // Constructor mặc định
    public User() {}

    // Constructor đầy đủ
    public User(int id, String username, String password, String fullName, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}