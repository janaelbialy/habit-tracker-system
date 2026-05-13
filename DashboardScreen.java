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

public class DashboardScreen {
    private Scene scene;
    private Stage stage;
    private VBox habitsContainer;

    // ── Nude Palette ─────────────────────────────────────────────
    private static final String BG = "#f5ede4"; // warm cream
    private static final String NAVBAR_BORDER = "#d4b89a"; // soft tan divider

    private static final String SIDEBAR_BG = "#c9a98a"; // camel
    private static final String SIDEBAR_TITLE = "#fdf8f4"; // ivory — visible on camel
    private static final String SIDEBAR_ACTIVE = "#fdf8f4"; // ivory active button
    private static final String SIDEBAR_ACTIVE_FG = "#3d2b1f"; // espresso on ivory
    private static final String SIDEBAR_FG = "#3d2b1f"; // espresso inactive text

    private static final String LOGO_FG = "#3d2b1f"; // espresso logo
    private static final String USER_FG = "#6b4c35"; // medium brown

    private static final String TITLE_FG = "#3d2b1f"; // page heading
    private static final String MUTED_FG = "#8a6a54"; // secondary text

    private static final String CARD_BG = "#fdf8f4"; // ivory card
    private static final String CARD_BORDER = "#d4b89a"; // tan border
    private static final String CARD_BG_DONE = "#eae0d5"; // darker warm done card
    private static final String CARD_BORDER_DONE = "#9c6b3c"; // brown done border

    private static final String BADGE_BG = "#e8d9cc"; // linen badge
    private static final String BADGE_FG = "#5a3820"; // dark brown badge text
    private static final String HABIT_NAME_FG = "#3d2b1f"; // espresso
    private static final String HABIT_NAME_DONE = "#5a3820"; // darker when done
    private static final String DESC_FG = "#8a6a54"; // muted warm brown
    private static final String STREAK_FG = "#9c6b3c"; // warm brown streak
    private static final String BEST_FG = "#b09880"; // dusty taupe

    private static final String CHECKIN_BG = "#9c6b3c"; // warm brown button
    private static final String CHECKIN_FG = "#fdf8f4"; // ivory text
    private static final String CHECKIN_DONE_BG = "#c9a98a"; // muted camel done
    private static final String CHECKIN_DONE_FG = "#fdf8f4"; // ivory done text
    private static final String DELETE_FG = "#8b2500"; // terracotta red
    // ─────────────────────────────────────────────────────────────

    public DashboardScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        root.setTop(buildNavBar());
        root.setLeft(buildSidebar());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        scrollPane.setFitToWidth(true);

        habitsContainer = new VBox(15);
        habitsContainer.setPadding(new Insets(20));
        habitsContainer.setStyle("-fx-background-color: " + BG + ";");

        loadHabits();

        scrollPane.setContent(habitsContainer);
        root.setCenter(scrollPane);

        scene = new Scene(root, 900, 650);
    }

    private HBox buildNavBar() {
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle("-fx-background-color: " + BG + "; "
                + "-fx-border-color: " + NAVBAR_BORDER + "; -fx-border-width: 0 0 2 0;");

        Text logo = new Text("🌿 Habit Tracker");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo.setFill(Color.web(LOGO_FG));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String username = MainApp.userService.getCurrentUser().getUsername();
        Label userLabel = new Label("👤 " + username);
        userLabel.setTextFill(Color.web(USER_FG));
        userLabel.setFont(Font.font("Arial", 13));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + LOGO_FG + "; "
                + "-fx-border-color: " + NAVBAR_BORDER + "; -fx-border-radius: 5; "
                + "-fx-cursor: hand; -fx-padding: 5 12; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> {
            MainApp.userService.logout();
            stage.setScene(new LoginScreen(stage).getScene());
        });

        navbar.getChildren().addAll(logo, spacer, userLabel, new Label("  "), logoutBtn);
        return navbar;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20, 15, 20, 15));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        Label menuTitle = new Label("MENU");
        menuTitle.setTextFill(Color.web(SIDEBAR_TITLE));
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #b8926e;");

        Button dashboardBtn = createSidebarButton("📋  My Habits", true);
        Button addBtn = createSidebarButton("➕  Add Habit", false);
        Button statsBtn = createSidebarButton("📊  Statistics", false);

        addBtn.setOnAction(e -> stage.setScene(new AddHabitScreen(stage, this).getScene()));

        statsBtn.setOnAction(e -> {
            int userId = MainApp.userService.getCurrentUser().getId();
            List<Habit> habits = MainApp.habitService.getUserHabits(userId);
            stage.setScene(new StatsScreen(stage, habits).getScene());
        });

        sidebar.getChildren().addAll(menuTitle, sep, dashboardBtn, addBtn, statsBtn);
        return sidebar;
    }

    public void loadHabits() {
        habitsContainer.getChildren().clear();

        int userId = MainApp.userService.getCurrentUser().getId();
        List<Habit> habits = MainApp.habitService.getUserHabits(userId);

        Text header = new Text("Today's Habits");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        header.setFill(Color.web(TITLE_FG));
        habitsContainer.getChildren().add(header);

        if (habits.isEmpty()) {
            Label empty = new Label("No habits yet! Click '➕ Add Habit' to get started.");
            empty.setTextFill(Color.web(MUTED_FG));
            empty.setFont(Font.font("Arial", 15));
            habitsContainer.getChildren().add(empty);
            return;
        }

        for (Habit habit : habits) {
            habitsContainer.getChildren().add(buildHabitCard(habit));
        }
    }

    private HBox buildHabitCard(Habit habit) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setAlignment(Pos.CENTER_LEFT);

        boolean completedToday = MainApp.habitService.isCompletedToday(habit.getId());
        Streak streak = MainApp.habitService.getStreak(habit.getId());

        card.setStyle("-fx-background-color: " + (completedToday ? CARD_BG_DONE : CARD_BG) + "; "
                + "-fx-background-radius: 10; "
                + "-fx-border-color: " + (completedToday ? CARD_BORDER_DONE : CARD_BORDER) + "; "
                + "-fx-border-radius: 10; -fx-border-width: 1.5;");

        Label categoryBadge = new Label(habit.getCategory());
        categoryBadge.setStyle("-fx-background-color: " + BADGE_BG + "; -fx-text-fill: " + BADGE_FG + "; "
                + "-fx-background-radius: 4; -fx-padding: 3 8; "
                + "-fx-font-size: 11; -fx-font-weight: bold;");

        VBox info = new VBox(4);

        Label nameLabel = new Label(habit.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(completedToday ? Color.web(HABIT_NAME_DONE) : Color.web(HABIT_NAME_FG));

        Label descLabel = new Label(habit.getDescription() != null ? habit.getDescription() : "");
        descLabel.setTextFill(Color.web(DESC_FG));
        descLabel.setFont(Font.font("Arial", 12));

        HBox streakBox = new HBox(5);
        Label streakLabel = new Label("🔥 " + streak.getCurrentStreak() + " day streak");
        streakLabel.setTextFill(Color.web(STREAK_FG));
        streakLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label bestLabel = new Label("  Best: " + streak.getLongestStreak());
        bestLabel.setTextFill(Color.web(BEST_FG));
        streakBox.getChildren().addAll(streakLabel, bestLabel);

        info.getChildren().addAll(categoryBadge, nameLabel, descLabel, streakBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button checkInBtn = new Button(completedToday ? "✅ Done!" : "Check In");
        checkInBtn.setDisable(completedToday);
        checkInBtn.setStyle(completedToday
                ? "-fx-background-color: " + CHECKIN_DONE_BG + "; -fx-text-fill: " + CHECKIN_DONE_FG + "; "
                        + "-fx-background-radius: 8; -fx-padding: 8 16; -fx-font-weight: bold;"
                : "-fx-background-color: " + CHECKIN_BG + "; -fx-text-fill: " + CHECKIN_FG + "; "
                        + "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16;");

        checkInBtn.setOnAction(e -> {
            if (MainApp.habitService.checkInHabit(habit.getId())) {
                loadHabits();
                showAlert("✅ Check-In!",
                        "Great job completing '" + habit.getName() + "' today!\nKeep the streak going!");
            }
        });

        Button deleteBtn = new Button("🗑");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + DELETE_FG + "; "
                + "-fx-cursor: hand; -fx-font-size: 14;");
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete habit '" + habit.getName() + "'?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    MainApp.habitService.deleteHabit(habit.getId());
                    loadHabits();
                }
            });
        });

        card.getChildren().addAll(info, spacer, checkInBtn, deleteBtn);
        return card;
    }

    private Button createSidebarButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(170);
        btn.setAlignment(Pos.CENTER_LEFT);
        String style = active
                ? "-fx-background-color: " + SIDEBAR_ACTIVE + "; -fx-text-fill: " + SIDEBAR_ACTIVE_FG + "; "
                        + "-fx-font-weight: bold;"
                : "-fx-background-color: transparent; -fx-text-fill: " + SIDEBAR_FG + ";";
        btn.setStyle(style + "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 15; -fx-font-size: 13;");
        return btn;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }
}