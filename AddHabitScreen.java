package com.habittracker.ui;

import com.habittracker.MainApp;
import com.habittracker.model.Habit;
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

public class AddHabitScreen {
    private Scene scene;
    private Stage stage;
    private DashboardScreen dashboardScreen;

    // ── Nude Palette ─────────────────────────────────────────────
    private static final String BG = "#f5ede4"; // warm cream
    private static final String SURFACE = "#fdf8f4"; // soft ivory inputs
    private static final String BORDER = "#d4b89a"; // tan border
    private static final String TITLE_FG = "#3d2b1f"; // espresso heading
    private static final String LABEL_FG = "#6b4c35"; // medium brown labels
    private static final String PROMPT_FG = "#b09880"; // dusty taupe placeholder
    private static final String INPUT_FG = "#3d2b1f"; // espresso typed text
    private static final String BACK_FG = "#6b4c35"; // medium brown back btn
    private static final String RADIO_FG = "#3d2b1f"; // espresso radio text
    private static final String BTN_BG = "#9c6b3c"; // warm brown
    private static final String BTN_FG = "#fdf8f4"; // ivory text
    private static final String ERROR_FG = "#8b2500"; // terracotta red
    // ─────────────────────────────────────────────────────────────

    public AddHabitScreen(Stage stage, DashboardScreen dashboardScreen) {
        this.stage = stage;
        this.dashboardScreen = dashboardScreen;
        buildUI();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        // Top bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: " + BG + "; "
                + "-fx-border-color: " + BORDER + "; -fx-border-width: 0 0 2 0;");

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + BACK_FG + "; "
                + "-fx-cursor: hand; -fx-font-size: 14; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> stage.setScene(dashboardScreen.getScene()));

        Text title = new Text("   ➕ Add New Habit");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.web(TITLE_FG));

        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // Form
        VBox form = new VBox(18);
        form.setPadding(new Insets(40));
        form.setMaxWidth(500);
        form.setAlignment(Pos.TOP_CENTER);

        Label nameLabel = createLabel("Habit Name *");
        TextField nameField = createTextField("e.g. Morning Run, Read 20 pages...");

        Label descLabel = createLabel("Description");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Optional description...");
        descArea.setPrefRowCount(3);
        descArea.setStyle("-fx-background-color: " + SURFACE + "; -fx-text-fill: " + INPUT_FG + "; "
                + "-fx-prompt-text-fill: " + PROMPT_FG + "; "
                + "-fx-background-radius: 8; -fx-border-color: " + BORDER + "; -fx-border-radius: 8;");

        Label categoryLabel = createLabel("Category");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Health", "Fitness", "Learning", "Productivity",
                "Mindfulness", "Finance", "Social", "General");
        categoryBox.setValue("General");
        categoryBox.setStyle("-fx-background-color: " + SURFACE + "; -fx-text-fill: " + INPUT_FG + "; "
                + "-fx-background-radius: 8; -fx-border-color: " + BORDER + ";");
        categoryBox.setPrefWidth(440);

        Label freqLabel = createLabel("Frequency");
        ToggleGroup freqGroup = new ToggleGroup();
        RadioButton dailyBtn = new RadioButton("Daily");
        RadioButton weeklyBtn = new RadioButton("Weekly");
        dailyBtn.setToggleGroup(freqGroup);
        weeklyBtn.setToggleGroup(freqGroup);
        dailyBtn.setSelected(true);
        dailyBtn.setTextFill(Color.web(RADIO_FG));
        weeklyBtn.setTextFill(Color.web(RADIO_FG));
        HBox freqBox = new HBox(20, dailyBtn, weeklyBtn);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web(ERROR_FG));
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button saveBtn = new Button("✔ Create Habit");
        saveBtn.setPrefWidth(440);
        saveBtn.setPrefHeight(45);
        saveBtn.setStyle("-fx-background-color: " + BTN_BG + "; -fx-text-fill: " + BTN_FG + "; "
                + "-fx-font-weight: bold; -fx-font-size: 15; "
                + "-fx-background-radius: 8; -fx-cursor: hand;");

        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            String category = categoryBox.getValue();
            Habit.Frequency freq = dailyBtn.isSelected() ? Habit.Frequency.DAILY : Habit.Frequency.WEEKLY;

            if (name.isEmpty()) {
                errorLabel.setText("Habit name is required!");
                return;
            }

            int userId = MainApp.userService.getCurrentUser().getId();
            boolean success = MainApp.habitService.addHabit(userId, name, desc, category, freq);

            if (success) {
                dashboardScreen.loadHabits();
                stage.setScene(dashboardScreen.getScene());
            } else {
                errorLabel.setText("Failed to create habit. Please try again.");
            }
        });

        form.getChildren().addAll(
                nameLabel, nameField,
                descLabel, descArea,
                categoryLabel, categoryBox,
                freqLabel, freqBox,
                errorLabel, saveBtn);

        ScrollPane scroll = new ScrollPane(form);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        scroll.setFitToWidth(true);

        root.setCenter(scroll);
        scene = new Scene(root, 900, 650);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.web(LABEL_FG));
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(42);
        tf.setMaxWidth(440);
        tf.setStyle("-fx-background-color: " + SURFACE + "; -fx-text-fill: " + INPUT_FG + "; "
                + "-fx-prompt-text-fill: " + PROMPT_FG + "; "
                + "-fx-background-radius: 8; -fx-border-color: " + BORDER + "; "
                + "-fx-border-radius: 8; -fx-padding: 8 12;");
        return tf;
    }

    public Scene getScene() {
        return scene;
    }
}