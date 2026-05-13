package com.habittracker.dao;

import com.habittracker.db.DatabaseConnection;
import com.habittracker.model.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabitDAO {

    public boolean createHabit(Habit habit) {
        String sql = "INSERT INTO habits (user_id, name, description, category, frequency) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, habit.getUserId());
            stmt.setString(2, habit.getName());
            stmt.setString(3, habit.getDescription());
            stmt.setString(4, habit.getCategory());
            stmt.setString(5, habit.getFrequency().name());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) habit.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error creating habit: " + e.getMessage());
        }
        return false;
    }

    public List<Habit> getHabitsByUserId(int userId) {
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT * FROM habits WHERE user_id = ? AND is_active = TRUE ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                habits.add(mapResultSetToHabit(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching habits: " + e.getMessage());
        }
        return habits;
    }

    public Optional<Habit> findById(int habitId) {
        String sql = "SELECT * FROM habits WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToHabit(rs));
        } catch (SQLException e) {
            System.err.println("❌ Error finding habit: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean updateHabit(Habit habit) {
        String sql = "UPDATE habits SET name=?, description=?, category=?, frequency=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, habit.getName());
            stmt.setString(2, habit.getDescription());
            stmt.setString(3, habit.getCategory());
            stmt.setString(4, habit.getFrequency().name());
            stmt.setInt(5, habit.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating habit: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteHabit(int habitId) {
        // Soft delete - just mark as inactive
        String sql = "UPDATE habits SET is_active = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting habit: " + e.getMessage());
        }
        return false;
    }

    private Habit mapResultSetToHabit(ResultSet rs) throws SQLException {
        return new Habit(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("category"),
            Habit.Frequency.valueOf(rs.getString("frequency")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getBoolean("is_active")
        );
    }
}
