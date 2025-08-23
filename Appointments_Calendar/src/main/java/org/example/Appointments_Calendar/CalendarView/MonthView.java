package org.example.Appointments_Calendar.CalendarView;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.Appointments_Calendar.Resorces.WeekDays;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class for monthly layout view on center pane
 */

public class MonthView extends CalendarView {

    private final FlowPane centerPane;

    private final List<WeekDays> weekDays = Arrays.asList(WeekDays.values());
    private final ZonedDateTime zdt;
    private final double width, height, border;
    private final int monthRows, monthColumns, yearColumns, year, monthCount, month;
    List<VBox> eachDayInAMonthList;

    public MonthView(double width, double height, double start, int year, int month, ZonedDateTime zdt) throws FileNotFoundException {
        super(width, height, start);
        CalendarView calendarView = new CalendarView(width, height, start);
        centerPane = calendarView.getCenterPane();
        this.width = width;
        this.height = height;
        this.zdt = zdt;
        this.year = year;
        this.month = month;
        border = calendarView.getBorder();
        monthRows = calendarView.getMonthRows();
        monthColumns = calendarView.getMonthColumns();
        yearColumns = calendarView.getYearColumns();
        monthCount = month;
        getMonthLayout();
    }

    @Override
    public FlowPane getCenterPane() {
        return centerPane;
    }

    private void getMonthLayout() throws FileNotFoundException {
        int firstDay = getFirstDay(zdt, year, monthCount);
        int lastDay = getLastDay(zdt, year, monthCount);
        VBox daysInThisMonth;
        String LAYOUT = "MONTH";
        for (int i = 0; i < monthColumns; i++) {

            centerPane.getChildren().add(new WeekDaysNames(width, border, monthColumns, yearColumns, LAYOUT, zdt).getFirstRow(weekDays.get(i).toString()));
        }
        setDay();
        MonthlyLayout.startOver(zdt, year, month);
        eachDayInAMonthList = new ArrayList<>();
        int count = 1;
        for (int i = 0; i < monthRows - 1; i++) {
            for (int j = 0; j < monthColumns; j++) {
                String day = getDaysInAMonth(year, monthCount, count++, firstDay, lastDay);

                daysInThisMonth = new MonthlyLayout(width, height, border, monthRows, monthColumns, yearColumns, LAYOUT, year, month).createLayout(day, firstDay, count);

                centerPane.getChildren().add(daysInThisMonth);
                if (!day.isEmpty()) {
                    eachDayInAMonthList.add(daysInThisMonth);
                }
            }
        }
    }

    public List<VBox> getEachDayList() {
        return eachDayInAMonthList;
    }
}
