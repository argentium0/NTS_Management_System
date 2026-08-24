package oopProject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main Application launcher for the JavaFX NTS Management System.
 */
public class NTSApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize SQLite Database Singleton Connection & Schemas
        DatabaseManager.getInstance();

        // Construct foundational UI shell
        MainLayout mainLayout = new MainLayout();

        // Create 1280x768 window scene
        Scene scene = new Scene(mainLayout.getRootPane(), 1280, 768);

        // Load stylesheet
        URL cssResource = getClass().getResource("styles.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("[NTSApp] Warning: styles.css not found in package directory.");
        }

        // Configure Primary Stage Window Taskbar Icon
        try {
            URL iconResource = getClass().getResource("logo.png");
            if (iconResource == null) {
                iconResource = getClass().getResource("/logo.png");
            }
            if (iconResource != null) {
                primaryStage.getIcons().add(new Image(iconResource.toExternalForm()));
            }
        } catch (Exception e) {
            System.err.println("[NTSApp] Note: Unable to load window icon: " + e.getMessage());
        }

        // Configure Primary Stage Window
        primaryStage.setTitle("NTS Management System - Desktop Portal");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
