package com.habittracker.model;

import java.time.LocalDateTime;

public class Habit {

    public enum Frequency {
        DAILY, WEEKLY
    }

    private int id;
    private int userId;
    private String name;
    private String description;
    private String category;
    private Frequency frequency;
    private LocalDateTime createdAt;
    private boolean isActive;

    // Constructor for new habit
    public Habit(int userId, String name, String description, String category, Frequency frequency) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.frequency = frequency;
        this.isActive = true;
    }
    public Habit(int id, int userId, String name, String description, String category,  
                 Frequency frequency, LocalDateTime createdAt, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.frequency = frequency;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Frequency getFrequency() { return frequency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Habit{id=" + id + ", name='" + name + "', category='" + category + "', frequency=" + frequency + "}";
    }
}
