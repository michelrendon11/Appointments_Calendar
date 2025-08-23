package org.example.Appointments_Calendar.CalendarView;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.Appointments_Calendar.AppointmentHandler.AppointmentHandler;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;

/**
 * Main class to set the monthly layout for month and year views
 */

public class MonthlyLayout {

    private static int lastDay, startOver;
    private final double width, height, border;
    private final int monthRows, monthColumns, yearColumns, month, year, yearToday, monthToday, dayToday;
    private final String LAYOUT;

    public MonthlyLayout(double width, double height, double border, int monthRows, int monthColumns, int yearColumns, String LAYOUT, int year, int month) {
        this.width = width;
        this.height = height;
        this.LAYOUT = LAYOUT;
        this.border = border;
        this.monthRows = monthRows;
        this.monthColumns = monthColumns;
        this.yearColumns = yearColumns;
        this.month = month;
        this.year = year;
        ZonedDateTime today = ZonedDateTime.now();
        yearToday = today.getYear();
        monthToday = today.getMonth().getValue();
        dayToday = today.getDayOfMonth();
    }

    protected static void startOver(ZonedDateTime zdt, int year, int month) {
        startOver = 1;
        int lastMonth = month == 1 ? 12 : month - 1;

        lastDay = ZonedDateTime.of(year, lastMonth, 1, 0, 0, 0, 0, zdt.getZone()).getMonth().maxLength();

        if (lastMonth == 2 && lastDay == 29 && !((year % 4 == 0 && year % 100 != 0) || (year % 4 == 0 && year % 400 == 0))) {

            lastDay = 28;
        }
    }

    protected VBox createLayout(String day, int firstDay, int count) throws FileNotFoundException {
        boolean isToday;
        VBox pane = new VBox();
        Text text = new Text();
        text.setText(day);

        boolean busy = AppointmentHandler.checkIfExist(String.valueOf(year), String.valueOf(month), day, false);

        if (!day.isEmpty() && year == yearToday && month == monthToday && Integer.parseInt(day) == dayToday) {

            text.setId("TodayDay");
            isToday = true;
        } else {
            isToday = false;
        }
        switch (LAYOUT) {
            case "MONTH" -> monthPane(pane, isToday, text, count, firstDay, busy);
            case "YEAR" -> yearPane(pane, isToday, text, busy);
        }
        pane.getChildren().add(text);
        return pane;
    }

    private void yearPane(VBox pane, boolean isToday, Text text, boolean busy) {
        pane.setAlignment(Pos.CENTER);
        pane.setPrefWidth(width / yearColumns / monthColumns - border / 2);
        pane.setPrefHeight(height / monthRows / 4);
        if (busy && isToday) {
            pane.setId("TodayPaneYearBusy");
        } else if (!busy && isToday) {
            pane.setId("TodayPaneYear");
        } else if (busy) {
            pane.setId("BusyPaneYear");
        } else if (!text.getText().isEmpty()) {
            pane.setId("MonthEachPane");
        }
    }

    private void monthPane(VBox pane, boolean isToday, Text text, int count, int firstDay, boolean busy) {
        pane.setPrefWidth(width / monthColumns - border);
        pane.setPrefHeight(height / monthRows - border);
        if (!busy && isToday) {
            pane.setId("TodayPaneMonthToday");
        } else if (busy && isToday) {
            pane.setId("TodayPaneMonthBusyToday");
        } else if (busy) {
            pane.setId("TodayPaneMonthBusy");
        } else if (!text.getText().isEmpty()) {
            pane.setId("homeAppointmentView");
        } else if (text.getText().isEmpty()) {
            if (count < lastDay) {
                text.setText(String.valueOf(lastDay - firstDay + count));
            } else if (count > lastDay) {
                text.setText(String.valueOf(startOver++));
            }
            pane.setId("EmptyPane");
            text.setId("EmptyText");
        }
    }
}
