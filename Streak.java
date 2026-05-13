package com.habittracker.model;

import java.time.LocalDate;

public class Streak {
    private int id;
    private int habitId;
    private int currentStreak;
    private int longestStreak;
    private LocalDate lastCompletedDate;

    public Streak(int habitId) {
        this.habitId = habitId;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.lastCompletedDate = null;
    }

    public Streak(int id, int habitId, int currentStreak, int longestStreak, LocalDate lastCompletedDate) {
        this.id = id;
        this.habitId = habitId;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastCompletedDate = lastCompletedDate;
    }

    // Getters
    public int getId() { return id; }
    public int getHabitId() { return habitId; }
    public int getCurrentStreak() { return currentStreak; }
    public int getLongestStreak() { return longestStreak; }
    public LocalDate getLastCompletedDate() { return lastCompletedDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
        if (currentStreak > longestStreak) {
            this.longestStreak = currentStreak; // Auto-update longest streak
        }
    }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
    public void setLastCompletedDate(LocalDate lastCompletedDate) { this.lastCompletedDate = lastCompletedDate; }

    public String getStreakDisplay() {
        return currentStreak > 0 ? "🔥 " + currentStreak + " days" : "No streak yet";
    }

    @Override
    public String toString() {
        return "Streak{habitId=" + habitId + ", current=" + currentStreak + ", longest=" + longestStreak + "}";
    }
}
