package com.habittracker.service;

import com.habittracker.dao.UserDAO;
import com.habittracker.model.User;
import com.habittracker.util.PasswordUtil;

import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;
    private User currentUser; // Logged-in user (session)

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String username, String password, String email) {
        // Validation
        if (username == null || username.trim().length() < 3) {
            System.err.println("❌ Username must be at least 3 characters.");
            return false;
        }
        if (password == null || password.length() < 6) {
            System.err.println("❌ Password must be at least 6 characters.");
            return false;
        }
        if (!email.contains("@")) {
            System.err.println("❌ Invalid email.");
            return false;
        }
        if (userDAO.usernameExists(username)) {
            System.err.println("❌ Username already taken.");
            return false;
        }
        if (userDAO.emailExists(email)) {
            System.err.println("❌ Email already registered.");
            return false;
        }

        String hashedPassword = PasswordUtil.hash(password);
        User newUser = new User(username.trim(), hashedPassword, email.trim().toLowerCase());
        return userDAO.createUser(newUser);
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) {
            System.err.println("❌ User not found.");
            return false;
        }

        User user = userOpt.get();
        if (PasswordUtil.verify(password, user.getPasswordHash())) {
            this.currentUser = user;
            System.out.println("✅ Welcome, " + user.getUsername() + "!");
            return true;
        } else {
            System.err.println("❌ Incorrect password.");
            return false;
        }
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("👋 Logged out.");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
