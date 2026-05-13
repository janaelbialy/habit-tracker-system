package com.habittracker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // ✅ JDBC URL مع السماح باسترجاع الـ Public Key
    private static final String URL = "jdbc:mysql://localhost:3306/habit_tracker_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // ✅ بيانات الدخول لحساب MySQL اللي أنشأناه
    private static final String USER = "habituser"; // write your MySQL username here
    private static final String PASSWORD = "jana123"; // write your MySQL password here

    private static Connection connection = null;

    private DatabaseConnection() {
    }

    // ✅ فتح الاتصال
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found!", e);
            }
        }
        return connection;
    }

    // ✅ غلق الاتصال
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
