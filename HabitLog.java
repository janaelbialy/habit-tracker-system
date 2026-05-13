package com.habittracker.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HabitLog {
    private int id;
    private int habitId;
    private LocalDate logDate;
    private boolean isCompleted;
    private LocalDateTime completedAt;

    public HabitLog(int habitId, LocalDate logDate) {
        this.habitId = habitId;
        this.logDate = logDate;
        this.isCompleted = false;
    }

    public HabitLog(int id, int habitId, LocalDate logDate, boolean isCompleted, LocalDateTime completedAt) {
        this.id = id;
        this.habitId = habitId;
        this.logDate = logDate;
        this.isCompleted = isCompleted;
        this.completedAt = completedAt;
    }

    public int getId() { return id; }
    public int getHabitId() { return habitId; }
    public LocalDate getLogDate() { return logDate; }
    public boolean isCompleted() { return isCompleted; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setId(int id) { this.id = id; }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
        if (completed) this.completedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "HabitLog{habitId=" + habitId + ", date=" + logDate + ", completed=" + isCompleted + "}";
    }
}
