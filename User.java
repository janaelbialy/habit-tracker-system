package com.habittracker.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private LocalDateTime createdAt;

    // Constructor for new user (before saving to DB)
    public User(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    // Constructor for loading from DB
    public User(int id, String username, String passwordHash, String email, LocalDateTime createdAt) {
        this.id = id;
        this.username = username; 
        this.passwordHash = passwordHash;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}
