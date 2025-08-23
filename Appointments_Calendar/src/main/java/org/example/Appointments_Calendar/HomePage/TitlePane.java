package org.example.Appointments_Calendar.HomePage;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Appointments_Calendar.AppointmentApplication;

/**
 * Title pane with name and main buttons for close, minimize and maximize
 */

public class TitlePane {

    private static boolean max = false;
    private final HBox pane, title, buttonsPane;
    private final Button close, minimize, maximize;
    private final double width;
    Stage stage;
    private double x = 0, y = 0, previousWidth = 0, previousHeight = 0;

    public TitlePane(double width) {
        this.width = width;
        pane = new HBox();
        title = new HBox();
        buttonsPane = new HBox();
        close = new Button();
        minimize = new Button();
        maximize = new Button();

        pane.setId("titlePane");
        title.setId("titleBox");
        buttonsPane.setId("titleButtons");
        close.setId("closeButton");
        minimize.setId("minimizeButton");
        maximize.setId("maximizeButton");
    }

    public HBox getTitlePane() {
        pane.setPrefWidth(width);
        pane.setPrefHeight(40);
        title.setPrefWidth(pane.getPrefWidth() / 2);
        buttonsPane.setPrefWidth(pane.getPrefWidth() / 2);
        setButtons();
        title.getChildren().add(setText());
        buttonsPane.getChildren().addAll(minimize, maximize, close, addSpace());
        pane.getChildren().addAll(title, buttonsPane);
        moveScene();
        return pane;
    }

    private void moveScene() {
        pane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) setMaximize();
            x = e.getSceneX();
            y = e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            stage = (Stage) pane.getScene().getWindow();
            if (!max) {
                stage.setX(e.getScreenX() - x);
                stage.setY(e.getScreenY() - y);
            }
        });
    }

    private void setButtons() {
        close.setText("X");
        close.setPrefWidth(30);
        minimize.setText("_");
        minimize.setPrefWidth(30);
        maximize.setText("↖");
        maximize.setPrefWidth(30);
        getButtonsBehaviour();
    }

    private Text setText() {
        Text text = new Text();
        text.setText(" ".repeat(5) + "Appointment's Calendar");
        text.setId("titleText");
        return text;
    }

    private Rectangle addSpace() {
        Rectangle rectangle = new Rectangle(10, 30);
        rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }

    private void getButtonsBehaviour() {
        close.setOnMouseClicked(_ -> setClose());
        minimize.setOnMouseClicked(_ -> setMinimize());
        maximize.setOnMouseClicked(_ -> setMaximize());
    }

    private void setMinimize() {
        stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    private void setMaximize() {
        stage = (Stage) maximize.getScene().getWindow();
        if (!max) {
            max = true;
            previousHeight = stage.getHeight();
            previousWidth = stage.getWidth();
            stage.setMaximized(true);
        } else {
            max = false;
            stage.setMaximized(false);
            stage.setHeight(previousHeight);
            stage.setWidth(previousWidth);
        }
    }

    private void setClose() {
        stage = (Stage) close.getScene().getWindow();
        if (AppointmentApplication.getStage() != null) {
            AppointmentApplication.closeStage();
        }
        stage.close();
    }
}
