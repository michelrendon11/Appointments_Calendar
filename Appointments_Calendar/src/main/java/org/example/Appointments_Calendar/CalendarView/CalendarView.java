package org.example.Appointments_Calendar.CalendarView;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

import java.time.ZonedDateTime;

/**
 * Main class for center pane year and months views, it takes the current window dimensions and sets  the first and last day of the current month for proper calendar display
 */

public class CalendarView {

    private final FlowPane centerPane;
    private final double border = 10;
    private int day;

    public CalendarView(double width, double height, double start) {
        centerPane = new FlowPane();
        centerPane.setPrefWidth(width - border * 2);
        centerPane.setPrefHeight(height - border);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setOrientation(Orientation.HORIZONTAL);
        centerPane.setLayoutX(border);
        centerPane.setHgap(5);
        centerPane.setVgap(5);
        centerPane.setLayoutY(start);
        centerPane.setLayoutX(border);
    }

    protected String getDaysInAMonth(int year, int monthCount, int count, int firstDay, int lastDay) {
        if (count < firstDay) {
            return "";
        } else if (day > lastDay) {
            return "";
        } else if (monthCount == 2 && day == 29 && !((year % 4 == 0 && year % 100 != 0) || (year % 4 == 0 && year % 400 == 0))) {

            return "";
        } else {
            return String.valueOf(day++);
        }
    }

    protected int getFirstDay(ZonedDateTime s, int year, int month) {
        int i = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, s.getZone()).getDayOfWeek().getValue();

        return i > 6 ? 1 : i + 1;
    }

    protected int getLastDay(ZonedDateTime s, int year, int monthCount) {
        return ZonedDateTime.of(year, monthCount, 1, 0, 0, 0, 0, s.getZone()).getMonth().maxLength();
    }

    protected void setDay() {
        day = 1;
    }

    protected FlowPane getCenterPane() {
        return centerPane;
    }

    protected double getBorder() {
        return border;
    }

    protected int getMonthRows() {
        return 7;
    }

    protected int getMonthColumns() {
        return 7;
    }

    protected int getYearRows() {
        return 3;
    }

    protected int getYearColumns() {
        return 4;
    }
}


