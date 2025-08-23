package org.example.Appointments_Calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * Main Entry for the Application
 **/

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("Home_Page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String style = Objects.requireNonNull(
                getClass().getResource("HomeStyleSheet.css")).toExternalForm();
        scene.getStylesheets().add(style);
        //Clear the custom Windows title bar
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}
