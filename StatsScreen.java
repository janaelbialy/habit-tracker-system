package com.habittracker.ui;

import com.habittracker.MainApp;
import com.habittracker.model.Habit;
import com.habittracker.model.Streak;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class StatsScreen {
    private Scene scene;
    private Stage stage;
    private List<Habit> habits;

    // ── Nude Palette ─────────────────────────────────────────────
    private static final String BG = "#f5ede4"; // warm cream page
    private static final String TOPBAR_BORDER = "#d4b89a"; // tan divider
    private static final String BACK_FG = "#6b4c35"; // medium brown back btn
    private static final String TITLE_FG = "#3d2b1f"; // espresso heading

    private static final String CARD_BG = "#fdf8f4"; // ivory summary card
    private static final String CARD_BORDER_1 = "#9c6b3c"; // warm brown — Total Habits
    private static final String CARD_BORDER_2 = "#8b2500"; // terracotta — Done Today
    private static final String CARD_BORDER_3 = "#c9a98a"; // camel — Best Streak
    private static final String CARD_VAL_1 = "#9c6b3c"; // warm brown value
    private static final String CARD_VAL_2 = "#8b2500"; // terracotta value
    private static final String CARD_VAL_3 = "#6b4c35"; // medium brown value
    private static final String CARD_LABEL = "#8a6a54"; // muted brown card label

    private static final String SECTION_FG = "#3d2b1f"; // section header
    private static final String SEP_COLOR = "#d4b89a"; // tan separator

    private static final String ROW_BG = "#fdf8f4"; // ivory habit row
    private static final String ROW_BORDER = "#d4b89a"; // tan row border
    private static final String NAME_FG = "#3d2b1f"; // espresso habit name
    private static final String META_FG = "#8a6a54"; // muted brown category/freq
    private static final String STREAK_FG = "#9c6b3c"; // warm brown current streak
    private static final String BEST_FG = "#6b4c35"; // medium brown best streak
    private static final String SUB_FG = "#b09880"; // dusty taupe sub-labels
    private static final String RATE_FG = "#6b4c35"; // medium brown rate label

    // Progress bar colors (low → mid → high)
    private static final String RATE_HIGH = "#4a6741"; // sage green ≥ 80%
    private static final String RATE_MID = "#9c6b3c"; // warm brown ≥ 50%
    private static final String RATE_LOW = "#8b2500"; // terracotta < 50%
    // ─────────────────────────────────────────────────────────────

    public StatsScreen(Stage stage, List<Habit> habits) {
        this.stage = stage;
        this.habits = habits;
        buildUI();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        // ── Top bar ──────────────────────────────────────────────
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: " + BG + "; "
                + "-fx-border-color: " + TOPBAR_BORDER + "; -fx-border-width: 0 0 2 0;");

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + BACK_FG + "; "
                + "-fx-cursor: hand; -fx-font-size: 14; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> stage.setScene(new DashboardScreen(stage).getScene()));

        Text title = new Text("   📊 Statistics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.web(TITLE_FG));

        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // ── Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        // Summary row
        HBox summaryRow = new HBox(15);
        summaryRow.setAlignment(Pos.CENTER_LEFT);

        int totalHabits = habits.size();
        long completedToday = habits.stream()
                .filter(h -> MainApp.habitService.isCompletedToday(h.getId()))
                .count();
        int bestStreak = habits.stream()
                .mapToInt(h -> MainApp.habitService.getStreak(h.getId()).getLongestStreak())
                .max().orElse(0);

        summaryRow.getChildren().addAll(
                buildSummaryCard("📋 Total Habits", String.valueOf(totalHabits), CARD_BORDER_1, CARD_VAL_1),
                buildSummaryCard("✅ Done Today", completedToday + "/" + totalHabits, CARD_BORDER_2, CARD_VAL_2),
                buildSummaryCard("🏆 Best Streak", bestStreak + " days", CARD_BORDER_3, CARD_VAL_3));

        // Per-habit header
        Text habitsHeader = new Text("Per-Habit Statistics (Last 30 Days)");
        habitsHeader.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        habitsHeader.setFill(Color.web(SECTION_FG));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + SEP_COLOR + ";");

        VBox habitStatsBox = new VBox(12);
        for (Habit habit : habits) {
            habitStatsBox.getChildren().add(buildHabitStatRow(habit));
        }

        content.getChildren().addAll(summaryRow, sep, habitsHeader, habitStatsBox);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        scene = new Scene(root, 900, 650);
    }

    private VBox buildSummaryCard(String label, String value, String borderColor, String valueColor) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: " + CARD_BG + "; "
                + "-fx-background-radius: 12; "
                + "-fx-border-color: " + borderColor + "; "
                + "-fx-border-radius: 12; -fx-border-width: 1.5; "
                + "-fx-effect: dropshadow(gaussian, rgba(100,60,20,0.10), 12, 0, 0, 3);");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(valueColor));

        Label nameLabel = new Label(label);
        nameLabel.setTextFill(Color.web(CARD_LABEL));
        nameLabel.setFont(Font.font("Arial", 12));

        card.getChildren().addAll(valueLabel, nameLabel);
        return card;
    }

    private HBox buildHabitStatRow(Habit habit) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(15, 20, 15, 20));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: " + ROW_BG + "; "
                + "-fx-background-radius: 10; "
                + "-fx-border-color: " + ROW_BORDER + "; "
                + "-fx-border-radius: 10; -fx-border-width: 1;");

        Streak streak = MainApp.habitService.getStreak(habit.getId());
        double rate = MainApp.habitService.getCompletionRate(habit.getId(), 30);

        // Habit name & category
        VBox info = new VBox(4);
        Label nameLabel = new Label(habit.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web(NAME_FG));

        Label categoryLabel = new Label(habit.getCategory() + " • " + habit.getFrequency());
        categoryLabel.setTextFill(Color.web(META_FG));
        categoryLabel.setFont(Font.font("Arial", 12));
        info.getChildren().addAll(nameLabel, categoryLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Current streak
        VBox streakBox = new VBox(2);
        streakBox.setAlignment(Pos.CENTER);
        Label currentStreakLabel = new Label("🔥 " + streak.getCurrentStreak());
        currentStreakLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        currentStreakLabel.setTextFill(Color.web(STREAK_FG));
        Label currentLabel = new Label("Current");
        currentLabel.setTextFill(Color.web(SUB_FG));
        currentLabel.setFont(Font.font("Arial", 11));
        streakBox.getChildren().addAll(currentStreakLabel, currentLabel);

        // Best streak
        VBox bestBox = new VBox(2);
        bestBox.setAlignment(Pos.CENTER);
        Label longestLabel = new Label("🏆 " + streak.getLongestStreak());
        longestLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        longestLabel.setTextFill(Color.web(BEST_FG));
        Label bestLabel = new Label("Best");
        bestLabel.setTextFill(Color.web(SUB_FG));
        bestLabel.setFont(Font.font("Arial", 11));
        bestBox.getChildren().addAll(longestLabel, bestLabel);

        // Completion rate bar
        VBox rateBox = new VBox(4);
        rateBox.setAlignment(Pos.CENTER);
        rateBox.setPrefWidth(120);
        Label rateLabel = new Label(String.format("%.0f%%", rate) + " (30d)");
        rateLabel.setTextFill(Color.web(RATE_FG));
        rateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        ProgressBar pb = new ProgressBar(rate / 100.0);
        pb.setPrefWidth(120);
        pb.setPrefHeight(10);
        pb.setStyle("-fx-accent: " + getRateColor(rate) + ";");

        rateBox.getChildren().addAll(rateLabel, pb);
        row.getChildren().addAll(info, spacer, streakBox, bestBox, rateBox);
        return row;
    }

    private String getRateColor(double rate) {
        if (rate >= 80)
            return RATE_HIGH; // sage green
        if (rate >= 50)
            return RATE_MID; // warm brown
        return RATE_LOW; // terracotta
    }

    public Scene getScene() {
        return scene;
    }
}