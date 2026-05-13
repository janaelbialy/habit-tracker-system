-- =============================================
-- Habit Tracker Database Schema
-- Run this file first before starting the app
-- =============================================

CREATE DATABASE IF NOT EXISTS habit_tracker_db;
USE habit_tracker_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL ,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Habits Table
CREATE TABLE IF NOT EXISTS habits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) DEFAULT 'General',
    frequency ENUM('DAILY', 'WEEKLY') DEFAULT 'DAILY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Habit Logs Table (tracks daily completions)
CREATE TABLE IF NOT EXISTS habit_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    habit_id INT NOT NULL,
    log_date DATE NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    UNIQUE KEY unique_habit_date (habit_id, log_date)
);

-- Streaks Table
CREATE TABLE IF NOT EXISTS streaks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    habit_id INT NOT NULL UNIQUE,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    last_completed_date DATE NULL,
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE
);

-- =============================================
-- Sample Data (Optional - for testing)
-- =============================================
INSERT INTO users (username, password_hash, email) VALUES
('medo', SHA2('123456', 256), 'test@example.com');