package com.habittracker.service;

import com.habittracker.dao.HabitDAO;
import com.habittracker.dao.HabitLogDAO;
import com.habittracker.dao.StreakDAO;
import com.habittracker.model.Habit;
import com.habittracker.model.HabitLog;
import com.habittracker.model.Streak;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HabitService {
    private final HabitDAO habitDAO;
    private final HabitLogDAO habitLogDAO;
    private final StreakDAO streakDAO;

    public HabitService() {
        this.habitDAO = new HabitDAO();
        this.habitLogDAO = new HabitLogDAO();
        this.streakDAO = new StreakDAO();
    }

    // ==================== Habit CRUD ====================

    public boolean addHabit(int userId, String name, String description, String category, Habit.Frequency frequency) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("❌ Habit name cannot be empty.");
            return false;
        }

        Habit habit = new Habit(userId, name.trim(), description, category, frequency);
        boolean created = habitDAO.createHabit(habit);

        if (created) {
            // Auto-create a streak record for this habit
            Streak streak = new Streak(habit.getId());
            streakDAO.createStreak(streak);
            System.out.println("✅ Habit '" + name + "' created!");
        }
        return created;
    }

    public List<Habit> getUserHabits(int userId) {
        return habitDAO.getHabitsByUserId(userId);
    }

    public boolean updateHabit(Habit habit) {
        return habitDAO.updateHabit(habit);
    }

    public boolean deleteHabit(int habitId) {
        return habitDAO.deleteHabit(habitId);
    }

    // ==================== Check-In & Streak Engine ====================

    /**
     * Main method: Mark a habit as completed today and update the streak.
     */
    public boolean checkInHabit(int habitId) {
        LocalDate today = LocalDate.now();

        // Check if already checked in today
        Optional<HabitLog> existingLog = habitLogDAO.getLogForDate(habitId, today);
        if (existingLog.isPresent() && existingLog.get().isCompleted()) {
            System.out.println("⚠️ Already checked in for today!");
            return false;
        }

        // Create / update the log
        HabitLog log = new HabitLog(habitId, today);
        log.setCompleted(true);
        boolean saved = habitLogDAO.createOrUpdateLog(log);

        if (saved) {
            updateStreak(habitId, today);
        }
        return saved;
    }

    /**
     * Streak Algorithm:
     * - If last completion was yesterday → increment streak
     * - If last completion was today → no change
     * - If last completion was more than 1 day ago → reset streak to 1
     * - If never completed → start streak at 1
     */
    private void updateStreak(int habitId, LocalDate completionDate) {
        Optional<Streak> streakOpt = streakDAO.getStreakByHabitId(habitId);
        Streak streak;

        if (streakOpt.isEmpty()) {
            streak = new Streak(habitId);
            streak.setCurrentStreak(1);
            streak.setLastCompletedDate(completionDate);
            streakDAO.createStreak(streak);
            System.out.println("🔥 Streak started! Day 1!");
            return;
        }

        streak = streakOpt.get();
        LocalDate lastDate = streak.getLastCompletedDate();

        if (lastDate == null) {
            // First time ever
            streak.setCurrentStreak(1);
        } else if (lastDate.equals(completionDate.minusDays(1))) {
            // Completed yesterday → continue streak
            streak.setCurrentStreak(streak.getCurrentStreak() + 1);
            System.out.println("🔥 Streak: " + streak.getCurrentStreak() + " days!");
        } else if (lastDate.equals(completionDate)) {
            // Already done today → no change
            System.out.println("✅ Already counted today.");
            return;
        } else {
            // Missed one or more days → reset
            System.out.println("💔 Streak broken! Restarting from day 1.");
            streak.setCurrentStreak(1);
        }

        streak.setLastCompletedDate(completionDate);
        streakDAO.updateStreak(streak);
    }

    // ==================== Statistics ====================

    public Streak getStreak(int habitId) {
        return streakDAO.getStreakByHabitId(habitId).orElse(new Streak(habitId));
    }

    /**
     * Completion rate: completed days / total days since habit was created (max 30
     * days)
     */
    public double getCompletionRate(int habitId, int lastNDays) {
        int completed = habitLogDAO.countCompletedDays(habitId, lastNDays);
        return lastNDays > 0 ? (double) completed / lastNDays * 100 : 0;
    }

    public boolean isCompletedToday(int habitId) {
        Optional<HabitLog> log = habitLogDAO.getLogForDate(habitId, LocalDate.now());
        return log.map(HabitLog::isCompleted).orElse(false);
    }

    public List<HabitLog> getHabitHistory(int habitId, int lastNDays) {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(lastNDays);
        return habitLogDAO.getLogsForHabit(habitId, from, to);
    }
}
