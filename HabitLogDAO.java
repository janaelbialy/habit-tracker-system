package com.habittracker.dao;

import com.habittracker.db.DatabaseConnection;
import com.habittracker.model.HabitLog;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabitLogDAO {

    public boolean createOrUpdateLog(HabitLog log) {
        String sql = "INSERT INTO habit_logs (habit_id, log_date, is_completed, completed_at) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE is_completed=?, completed_at=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, log.getHabitId());
            stmt.setDate(2, Date.valueOf(log.getLogDate()));
            stmt.setBoolean(3, log.isCompleted());
            stmt.setTimestamp(4, log.isCompleted() ? Timestamp.valueOf(log.getCompletedAt()) : null);
            stmt.setBoolean(5, log.isCompleted());
            stmt.setTimestamp(6, log.isCompleted() ? Timestamp.valueOf(log.getCompletedAt()) : null);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error saving habit log: " + e.getMessage());
        }
        return false;
    }

    public Optional<HabitLog> getLogForDate(int habitId, LocalDate date) {
        String sql = "SELECT * FROM habit_logs WHERE habit_id = ? AND log_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("❌ Error fetching log: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<HabitLog> getLogsForHabit(int habitId, LocalDate from, LocalDate to) {
        List<HabitLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM habit_logs WHERE habit_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) logs.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("❌ Error fetching logs: " + e.getMessage());
        }
        return logs;
    }

    // Count completed days in the last N days (for completion rate)
    public int countCompletedDays(int habitId, int lastNDays) {
        String sql = "SELECT COUNT(*) FROM habit_logs WHERE habit_id = ? AND is_completed = TRUE " +
                     "AND log_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habitId);
            stmt.setInt(2, lastNDays);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error counting completed days: " + e.getMessage());
        }
        return 0;
    }

    private HabitLog mapResultSet(ResultSet rs) throws SQLException {
        Timestamp completedAt = rs.getTimestamp("completed_at");
        return new HabitLog(
            rs.getInt("id"),
            rs.getInt("habit_id"),
            rs.getDate("log_date").toLocalDate(),
            rs.getBoolean("is_completed"),
            completedAt != null ? completedAt.toLocalDateTime() : null
        );
    }
}
