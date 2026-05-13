package com.habittracker.dao;

import com.habittracker.db.DatabaseConnection;
import com.habittracker.model.Streak;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class StreakDAO {

    public boolean createStreak(Streak streak) {
        String sql = "INSERT INTO streaks (habit_id, current_streak, longest_streak, last_completed_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, streak.getHabitId());
            stmt.setInt(2, streak.getCurrentStreak());
            stmt.setInt(3, streak.getLongestStreak());
            stmt.setDate(4, streak.getLastCompletedDate() != null ? Date.valueOf(streak.getLastCompletedDate()) : null);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) streak.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error creating streak: " + e.getMessage());
        }
        return false;
    }

    public Optional<Streak> getStreakByHabitId(int habitId) {
        String sql = "SELECT * FROM streaks WHERE habit_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("❌ Error fetching streak: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean updateStreak(Streak streak) {
        String sql = "UPDATE streaks SET current_streak=?, longest_streak=?, last_completed_date=? WHERE habit_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, streak.getCurrentStreak());
            stmt.setInt(2, streak.getLongestStreak());
            stmt.setDate(3, streak.getLastCompletedDate() != null ? Date.valueOf(streak.getLastCompletedDate()) : null);
            stmt.setInt(4, streak.getHabitId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating streak: " + e.getMessage());
        }
        return false;
    }

    private Streak mapResultSet(ResultSet rs) throws SQLException {
        Date lastDate = rs.getDate("last_completed_date");
        return new Streak(
            rs.getInt("id"),
            rs.getInt("habit_id"),
            rs.getInt("current_streak"),
            rs.getInt("longest_streak"),
            lastDate != null ? lastDate.toLocalDate() : null
        );
    }
}
