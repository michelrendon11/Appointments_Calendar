package org.example.Appointments_Calendar.CalendarView;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

/**
 * class to display the day viw on center pane
 */

public class DayView extends CalendarView {

    private final FlowPane centerPane;

    private final ZonedDateTime zdt;
    private final List<HBox> appointmentList;

    private final double width, height, border;
    private final int monthColumns, startTime, end, year, month, day, weekNumber;

    public DayView(double width, double height, double start, ZonedDateTime zdt, int startTime, int endTime) throws FileNotFoundException {
        super(width, height, start);
        CalendarView calendarView = new CalendarView(width, height, start);
        centerPane = calendarView.getCenterPane();
        this.width = width;
        this.height = height;
        this.startTime = startTime;
        this.zdt = zdt;
        border = calendarView.getBorder();
        monthColumns = calendarView.getMonthColumns();
        end = (endTime - startTime) * 2 + 1;
        appointmentList = new ArrayList<>();
        year = zdt.getYear();
        month = zdt.getMonthValue();
        day = zdt.getDayOfMonth();
        weekNumber = zdt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        getDayLayout();
    }

    @Override
    public FlowPane getCenterPane() {
        return centerPane;
    }

    private void getDayLayout() throws FileNotFoundException {
        HourlyLayout hourlyLayout = new HourlyLayout(width, border, monthColumns, false);
        hourlyLayout.setCount(startTime - 1);
        ScrollPane scrollPane = new ScrollPane();
        VBox rows = new VBox();
        HBox appointments;
        HBox weekDayPane = new HBox();
        weekDayPane.setPrefWidth(width - border * 2);
        weekDayPane.setAlignment(Pos.CENTER);
        Text weekDay = new Text();
        weekDay.setId("WeekDayDayView");
        weekDay.setText(zdt.getDayOfWeek().toString());
        weekDayPane.getChildren().add(weekDay);
        scrollPane.setPrefHeight(height - border * 10);
        scrollPane.setId("ScrollPane");
        WeekDaysNumbers.startCount();
        for (int i = 0; i < end; i++) {
            appointments = hourlyLayout.getRows(year, month, day, weekNumber, zdt);
            appointmentList.add(appointments);
            rows.getChildren().add(appointments);
        }
        scrollPane.setContent(rows);
        centerPane.getChildren().addAll(weekDayPane, scrollPane);
    }

    public List<HBox> getAppointmentList() {
        return appointmentList;
    }
}
