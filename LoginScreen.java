package com.habittracker.ui;

import com.habittracker.MainApp;
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

public class LoginScreen {
    private Scene scene;
    private Stage stage;

    // ── Nude Palette ─────────────────────────────────────────────
    private static final String BG = "#f5ede4"; // warm cream background
    private static final String CARD_BG = "#fdf8f4"; // soft ivory card
    private static final String TITLE_FG = "#3d2b1f"; // deep espresso
    private static final String SUBTITLE_FG = "#8a6a54"; // muted warm brown
    private static final String INPUT_BG = "#faf4ee"; // lightest cream input
    private static final String INPUT_FG = "#3d2b1f"; // espresso typed text
    private static final String PROMPT_FG = "#b89e8a"; // dusty taupe placeholder
    private static final String BTN_BG = "#9c6b3c"; // warm brown button
    private static final String BTN_HOVER = "#7a5230"; // darker on hover
    private static final String BTN_FG = "#fdf8f4"; // ivory text on button
    private static final String ERROR_FG = "#8b2500"; // deep terracotta red
    private static final String SUCCESS_FG = "#4a6741"; // muted sage green
    private static final String TAB_BG = "#e8d9cc"; // warm linen tab
    private static final String BORDER = "#d4b89a"; // soft tan border
    // ─────────────────────────────────────────────────────────────

    public LoginScreen(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(380);
        card.setStyle("-fx-background-color: " + CARD_BG + "; "
                + "-fx-background-radius: 15; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 15; -fx-border-width: 1.5; "
                + "-fx-effect: dropshadow(gaussian, rgba(100,60,20,0.13), 24, 0, 0, 6);");

        Text title = new Text("🌿 Habit Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web(TITLE_FG));

        Text subtitle = new Text("Build habits. Break limits.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setFill(Color.web(SUBTITLE_FG));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        Tab loginTab = new Tab("🍂 Login");
        loginTab.setContent(buildLoginForm());
        loginTab.setStyle("-fx-background-color: " + TAB_BG + ";");

        Tab registerTab = new Tab("🌾 Register");
        registerTab.setContent(buildRegisterForm());
        registerTab.setStyle("-fx-background-color: " + TAB_BG + ";");

        tabPane.getTabs().addAll(loginTab, registerTab);
        card.getChildren().addAll(title, subtitle, tabPane);

        BorderPane.setAlignment(card, Pos.CENTER);
        root.setCenter(card);

        scene = new Scene(root, 900, 650);
    }

    private VBox buildLoginForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(20, 0, 10, 0));
        form.setAlignment(Pos.CENTER);

        TextField usernameField = createTextField("Username");
        PasswordField passwordField = createPasswordField("Password");
        Label errorLabel = createErrorLabel();
        Button loginBtn = createPrimaryButton("Login Now");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            boolean success = MainApp.userService.login(username, password);
            if (success) {
                navigateToDashboard();
            } else {
                errorLabel.setText("❌ Invalid username or password");
                passwordField.clear();
            }
        });

        form.getChildren().addAll(usernameField, passwordField, errorLabel, loginBtn);
        return form;
    }

    private VBox buildRegisterForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(20, 0, 10, 0));
        form.setAlignment(Pos.CENTER);

        TextField usernameField = createTextField("Username");
        TextField emailField = createTextField("Email");
        PasswordField passwordField = createPasswordField("Password (min 6 chars)");
        Label errorLabel = createErrorLabel();

        Label successLabel = new Label();
        successLabel.setTextFill(Color.web(SUCCESS_FG));
        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button registerBtn = createPrimaryButton("Create Account");

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                successLabel.setText("");
                return;
            }
            boolean success = MainApp.userService.register(username, password, email);
            if (success) {
                successLabel.setText("✅ Account created! Please login.");
                errorLabel.setText("");
                usernameField.clear();
                emailField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("Registration failed. Username/email may be taken.");
                successLabel.setText("");
            }
        });

        form.getChildren().addAll(usernameField, emailField, passwordField,
                errorLabel, successLabel, registerBtn);
        return form;
    }

    private void navigateToDashboard() {
        stage.setScene(new DashboardScreen(stage).getScene());
    }

    // ── UI Helpers ───────────────────────────────────────────────

    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(280);
        tf.setPrefHeight(40);
        tf.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + INPUT_FG + "; "
                + "-fx-prompt-text-fill: " + PROMPT_FG + "; "
                + "-fx-background-radius: 8; -fx-border-color: " + BORDER + "; "
                + "-fx-border-radius: 8; -fx-padding: 8 12;");
        return tf;
    }

    private PasswordField createPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefWidth(280);
        pf.setPrefHeight(40);
        pf.setStyle("-fx-background-color: " + INPUT_BG + "; -fx-text-fill: " + INPUT_FG + "; "
                + "-fx-prompt-text-fill: " + PROMPT_FG + "; "
                + "-fx-background-radius: 8; -fx-border-color: " + BORDER + "; "
                + "-fx-border-radius: 8; -fx-padding: 8 12;");
        return pf;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(280);
        btn.setPrefHeight(42);

        String normal = "-fx-background-color: " + BTN_BG + "; -fx-text-fill: " + BTN_FG + "; "
                + "-fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 8; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + BTN_HOVER + "; -fx-text-fill: " + BTN_FG + "; "
                + "-fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 8; -fx-cursor: hand;";

        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    private Label createErrorLabel() {
        Label label = new Label();
        label.setTextFill(Color.web(ERROR_FG));
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        return label;
    }

    public Scene getScene() {
        return scene;
    }
}