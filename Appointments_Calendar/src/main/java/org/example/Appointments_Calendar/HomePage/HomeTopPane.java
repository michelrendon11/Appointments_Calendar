package org.example.Appointments_Calendar.HomePage;

import javafx.scene.layout.HBox;

/**
 * Creates the top pane rectangle with fix height to latter accommodate the navigation buttons
 */

public class HomeTopPane {

    private final HBox topPane;
    private final double width, start;

    public HomeTopPane(double width, double start) {
        this.width = width;
        this.start = start;
        topPane = new HBox();
        topPane.setId("topPane");
    }

    public HBox getToPane() {
        topPane.setPrefWidth(width);
        topPane.setPrefHeight(80);
        topPane.setLayoutY(start);
        return topPane;
    }
}
