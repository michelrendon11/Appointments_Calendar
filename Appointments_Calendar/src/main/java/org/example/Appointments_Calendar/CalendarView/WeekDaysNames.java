package org.example.Appointments_Calendar.CalendarView;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.ZonedDateTime;

/**
 * helper class to set the right week days names for month, year, and week views
 */

public class WeekDaysNames {

    private final double width, border;
    private final int monthColumns, yearColumns;
    private final String LAYOUT;
    private final boolean isToday;
    ZonedDateTime zdt, today;
    private int todayDay;

    public WeekDaysNames(double width, double border, int monthColumns, int yearColumns, String LAYOUT, ZonedDateTime zdt) {
        this.width = width;
        this.border = border;
        this.monthColumns = monthColumns;
        this.yearColumns = yearColumns;
        this.LAYOUT = LAYOUT;
        this.zdt = zdt;
        isToday = checkIfToday();
        WeekDaysNumbers weekDaysNumbers = new WeekDaysNumbers(zdt);
    }

    private boolean checkIfToday() {
        today = ZonedDateTime.now();
        int yearToday = today.getYear();
        int monthToday = today.getMonthValue();
        todayDay = today.getDayOfMonth();
        int year = zdt.getYear();
        int month = zdt.getMonthValue();
        return year == yearToday && month == monthToday;
    }

    protected VBox getFirstRow(String s) {
        Text day = new Text();
        Text number = new Text();
        day.setText(s);
        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);
        switch (LAYOUT) {
            case "MONTH" -> {
                day.setId("CenterPaneWeekText");
                monthPane(pane);
            }
            case "YEAR" -> {
                day.setId("CenterPaneEachMonthWeekText");
                yearPane(pane);
            }
            case "WEEK" -> {
                day.setId("CenterPaneWeekText");
                number.setId("CenterPaneWeekText");
                number.setText(WeekDaysNumbers.getDayNumbers());
                weekPane(pane, number.getText());
            }
        }
        pane.getChildren().add(day);
        if (LAYOUT.equals("WEEK")) {
            pane.getChildren().add(number);
            pane.setPrefHeight(60);
        }
        return pane;
    }

    private void yearPane(VBox pane) {
        pane.setAlignment(Pos.CENTER);
        pane.setPrefWidth(width / yearColumns / monthColumns - border / 2);
        pane.setPrefHeight(5);
    }

    private void monthPane(VBox pane) {
        pane.setPrefHeight(30);
        pane.setPrefWidth(width / monthColumns - border);
    }

    private void weekPane(VBox pane, String number) {
        if (isToday && todayDay == Integer.parseInt(number)) pane.setId("TodayPane");
        else pane.setId("WeekViewWeekNames");
        pane.setPrefWidth(width / monthColumns - border);
    }
}
