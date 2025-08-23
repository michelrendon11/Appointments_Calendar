package org.example.Appointments_Calendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * Second window for appointments, this window is opened when the user clicks on an appointment
 **/

public class AppointmentApplication extends Application {

    static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    public static void closeStage() {
        AppointmentApplication.stage.close();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        AppointmentApplication.stage = stage;
        AppointmentApplication.stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(org.example.Appointments_Calendar.MainApplication.class.getResource("Appointment_Page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        String style = Objects.requireNonNull(getClass().getResource("AppointmentView.css")).toExternalForm();

        scene.getStylesheets().add(style);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        setStage(stage);
    }
}
