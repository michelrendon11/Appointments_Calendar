package org.example.Appointments_Calendar.CalendarView;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.Appointments_Calendar.Resorces.WeekDays;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * main class to set the week layout on center pane
 **/

public class WeekView extends CalendarView {

    private final FlowPane centerPane;

    private final List<WeekDays> weekDays = Arrays.asList(WeekDays.values());
    private final List<VBox> weekDaysNamesList = new ArrayList<>();
    private final ZonedDateTime zdt;
    private final double width, height, border;
    private final int yearColumns, monthColumns, end, startTime, year, month, day, weekNumber;
    private List<HBox> weekBoxes;

    public WeekView(double width, double height, double start, ZonedDateTime zdt, int startTime, int endTime) throws FileNotFoundException {
        super(width, height, start);
        CalendarView calendarView = new CalendarView(width, height, start);
        centerPane = calendarView.getCenterPane();
        this.width = width;
        this.height = height;
        this.zdt = zdt;
        this.startTime = startTime;
        border = calendarView.getBorder();
        yearColumns = calendarView.getYearColumns();
        monthColumns = calendarView.getMonthColumns();
        end = (endTime - startTime) * 2 + 1;
        year = zdt.getYear();
        month = zdt.getMonthValue();
        day = zdt.getDayOfMonth();
        weekNumber = zdt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        getWeekLayout();
    }

    @Override
    public FlowPane getCenterPane() {
        return centerPane;
    }

    private void getWeekLayout() throws FileNotFoundException {
        HourlyLayout hourlyLayout = new HourlyLayout(width, border, monthColumns, true);
        hourlyLayout.setCount(startTime - 1);
        ScrollPane scrollPane = new ScrollPane();
        HBox panel = new HBox();
        VBox rows = new VBox();
        HBox emptyPane = new HBox();
        emptyPane.setPrefWidth(110);
        scrollPane.setPrefHeight(height - border * 10);
        panel.setTranslateX(panel.getLayoutX() - 10);
        panel.setPrefWidth(width - border * 12);
        panel.setId("WeeksRows");
        scrollPane.setPrefWidth(panel.getPrefWidth() + border * 4);
        scrollPane.setId("ScrollPane");
        String LAYOUT = "WEEK";
        panel.getChildren().add(emptyPane);
        WeekDaysNumbers.startCount();

        for (int i = 0; i < monthColumns; i++) {

            VBox weekNames = new WeekDaysNames(width, border, monthColumns, yearColumns, LAYOUT, zdt).getFirstRow(weekDays.get(i).toString());

            panel.getChildren().add(weekNames);
            weekDaysNamesList.add(weekNames);
        }
        for (int i = 0; i < end; i++) {
            rows.getChildren().add(hourlyLayout.getRows(year, month, day, weekNumber, zdt));
        }
        weekBoxes = hourlyLayout.getWeekPanes();
        scrollPane.setContent(rows);
        centerPane.getChildren().add(panel);
        centerPane.getChildren().add(scrollPane);
    }

    public List<VBox> getWeekDaysNamesList() {
        return weekDaysNamesList;
    }

    public List<HBox> getWeekBoxes() {
        return weekBoxes;
    }
}
