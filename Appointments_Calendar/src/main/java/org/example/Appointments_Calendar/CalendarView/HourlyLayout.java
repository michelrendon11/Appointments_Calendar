package org.example.Appointments_Calendar.CalendarView;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.Appointments_Calendar.AppointmentHandler.AppointmentHandler;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Main class for hourly layout for day and week view
 */

public class HourlyLayout {

    private static int count;
    private final int monthColumns;
    private final double width, border;
    private final boolean isWeekLayout;
    private final List<HBox> weekPanes;
    private boolean half;
    private String AM;

    public HourlyLayout(double width, double border, int monthColumns, boolean isWeekLayout) {
        this.width = width;
        this.border = border;
        this.monthColumns = monthColumns;
        this.isWeekLayout = isWeekLayout;
        AM = "am";
        half = true;
        weekPanes = new ArrayList<>();
    }

    protected HBox getRows(int year, int month, int day, int weekNumber, ZonedDateTime zdt) throws FileNotFoundException {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        HBox first = new HBox();
        HBox row = new HBox();
        HBox appointments = new HBox();
        Text text = new Text();
        TextField appointment = new TextField();
        String halfHour;
        first.setId("WeekViewTime");
        row.setId("HourlyBoxesWeek");
        row.setPrefWidth(width - border * 12);
        first.setPrefWidth(110);
        appointments.setPrefWidth(row.getPrefWidth() - first.getPrefWidth() - border);
        appointment.setPrefWidth(appointments.getPrefWidth());
        appointment.setEditable(false);
        appointment.setPrefHeight(10);
        half = !half;
        if (half) {
            halfHour = ":30";
        } else {
            halfHour = ":00";
            count++;
        }
        if (count == 12) AM = "pm";
        if (count > 12) count = 1;
        text.setText(count + halfHour + " " + AM);
        text.setId("HourlyText");
        first.getChildren().add(text);
        row.getChildren().add(first);

        boolean busy = AppointmentHandler.checkIfExist(String.valueOf(year), String.valueOf(month), String.valueOf(day), text.getText(), true);

        if (busy) {
            appointments.setId("HourlyBoxesBusy");
            appointment.setId("HourlyBoxesBusyText");
        } else {
            appointments.setId("HourlyBoxes");
            appointment.setId("HourlyBoxesText");
        }
        if (isWeekLayout) {
            for (int i = 0; i < 7; i++) {

                zdt = ZonedDateTime.now().withYear(year).with(weekFields.weekOfYear(), weekNumber).with(weekFields.dayOfWeek(), i + 1);

                HBox box = getEachBox(text.getText(), zdt, zdt.getDayOfMonth());
                weekPanes.add(box);
                row.getChildren().add(box);
            }
        } else {

            appointment.setText(AppointmentHandler.getAppointment(String.valueOf(zdt.getYear()), String.valueOf(zdt.getMonthValue()), String.valueOf(zdt.getDayOfMonth()), text.getText()));

            appointments.getChildren().add(appointment);
            row.getChildren().add(appointments);
        }
        return row;
    }

    private HBox getEachBox(String time, ZonedDateTime zdt, int day) throws FileNotFoundException {

        boolean busy = AppointmentHandler.checkIfExist(String.valueOf(zdt.getYear()), String.valueOf(zdt.getMonthValue()), String.valueOf(day), time, true);

        HBox eachBox = new HBox();
        TextField text = new TextField();
        eachBox.setPrefWidth(width / monthColumns - border * 3);
        text.maxWidth(eachBox.getPrefWidth());
        text.setEditable(false);
        if (busy) {
            text.setText(getAppointment(zdt, time));
            text.setId("WeekAppointmentsBusyText");
            eachBox.setId("WeekAppointmentsBusy");
        } else {
            eachBox.setId("WeekAppointments");
            text.setId("WeekAppointmentsText");
        }
        eachBox.getChildren().add(text);
        text.setFocusTraversable(false);
        eachBox.requestFocus();
        return eachBox;
    }

    private String getAppointment(ZonedDateTime zdt, String time) throws FileNotFoundException {
        return (AppointmentHandler.getAppointment(String.valueOf(zdt.getYear()), String.valueOf(zdt.getMonthValue()), String.valueOf(zdt.getDayOfMonth()), time));
    }

    protected void setCount(int startTime) {
        count = startTime;
    }

    public List<HBox> getWeekPanes() {
        return weekPanes;
    }
}
