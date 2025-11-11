package com.danield.movemecursor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the entry point of the Application.
 */
public class App extends Application {

    private static Scene scene;
    public static final String TITLE = "MMC";
    public static final double MIN_HEIGHT = 85.0;
    public static final double MIN_WIDTH = 230.0;
    public static final String VERSION = "0.0.2";
    public static final String ICON_PATH = "DemoIcon.png";

    @Override
    public void start(Stage stage) {
        scene = new Scene(loadFXML(), MIN_WIDTH, MIN_HEIGHT);
        stage.getIcons().add(loadIcon());
        stage.setTitle(String.format("%s %s", TITLE, VERSION));
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    private static Parent loadFXML() {
        try {
            return new FXMLLoader(App.class.getResource("PrimaryView" + ".fxml")).load();
        } catch (IOException e) {
            System.err.println("Failed to load FXML");
            throw new RuntimeException(e);
        }
    }

    private static Image loadIcon() {
        return new Image(Objects.requireNonNull(App.class.getResourceAsStream(App.ICON_PATH)));
    }

    public static void main(String[] args) {
        launch();
        System.exit(0);
    }
}