package com.habittracker;

import com.habittracker.service.HabitService;
import com.habittracker.service.UserService;
import com.habittracker.ui.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Global services (shared across screens)
    public static UserService userService = new UserService();
    public static HabitService habitService = new HabitService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🔥 Habit Tracker");
        primaryStage.setWidth(900);
        primaryStage.setHeight(650);
        primaryStage.setResizable(false);

        // Start with Login Screen
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}