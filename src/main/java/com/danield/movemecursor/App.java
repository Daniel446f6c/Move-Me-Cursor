package com.danield.movemecursor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the entry point of the Application.
 * @author Daniel D
 */
public class App extends Application {

    private static Scene scene;
    public static final String TITLE = "MMC";
    public static final double MIN_HEIGHT = 48.0;
    public static final double MIN_WIDTH = 228.0;
    public static final String VERSION = "0.0.2";

    @Override
    public void start(Stage stage) throws Exception {
        
        scene = new Scene(loadFXML("PrimaryView"), MIN_WIDTH, MIN_HEIGHT);

        stage.getIcons().add(new Image(App.class.getResourceAsStream("DemoIcon.png")));
        stage.setTitle(String.format("%s %s", TITLE, VERSION));
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        System.exit(0);
    }

}