package org.example.Appointments_Calendar.CalendarView;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.Appointments_Calendar.Resorces.Months;
import org.example.Appointments_Calendar.Resorces.WeekDays;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * main class to set the year layout on center pane
 **/

public class YearView extends CalendarView {

    private final FlowPane centerPane;
    private final ZonedDateTime zdt;

    private final double width, height, border;
    private final int monthRows, monthColumns, yearRows, yearColumns, year, month;

    private final List<WeekDays> weekDays = Arrays.asList(WeekDays.values());
    private final List<Months> months = Arrays.asList(Months.values());
    private final Map<HBox, List<VBox>> daysInAMonthList;
    private List<Node> yearPaneBoxes;

    public YearView(double width, double height, double start, int year, int month, ZonedDateTime zdt) throws FileNotFoundException {

        super(width, height, start);

        CalendarView calendarView = new CalendarView(width, height, start);
        centerPane = calendarView.getCenterPane();
        this.width = width;
        this.height = height;
        this.year = year;
        this.month = month;
        this.zdt = zdt;
        border = calendarView.getBorder();
        monthRows = calendarView.getMonthRows();
        monthColumns = calendarView.getMonthColumns();
        yearColumns = calendarView.getYearColumns();
        yearRows = calendarView.getYearRows();
        yearPaneBoxes = new ArrayList<>();
        daysInAMonthList = new LinkedHashMap<>();
        getYearLayout();
    }

    @Override
    public FlowPane getCenterPane() {
        return centerPane;
    }

    private void getYearLayout() throws FileNotFoundException {
        int monthEnum = 0;
        int monthCount = 0;
        List<Node> monthsList = new ArrayList<>();

        for (int i = 0; i < yearColumns; i++) {
            for (int j = 0; j < yearRows; j++) {
                FlowPane pane = new FlowPane();
                HBox box = new HBox();
                VBox daysInThisMonth;
                Text text = new Text();
                text.setId("monthInAYear");
                pane.setAlignment(Pos.CENTER);
                pane.setPrefWidth(width / yearColumns - border);
                pane.setPrefHeight(height / yearRows - border);
                box.setPrefWidth(pane.getPrefWidth() - border);
                box.setPrefHeight(10);
                box.setAlignment(Pos.CENTER);
                box.setId("MonthsInYear");
                text.setText(months.get(monthEnum++).toString());
                monthCount++;
                int firstDay = getFirstDay(zdt, year, monthCount);
                int lastDay = getLastDay(zdt, year, monthCount);
                box.getChildren().add(text);
                pane.getChildren().add(box);
                monthsList.add(box);
                String LAYOUT = "YEAR";
                for (int l = 0; l < monthColumns; l++) {

                    pane.getChildren().add(new WeekDaysNames(width, border, monthColumns, yearColumns, LAYOUT, zdt).getFirstRow(weekDays.get(l).toString()));
                }
                int count = 1;
                setDay();
                MonthlyLayout.startOver(zdt, year, month);
                List<VBox> eachDayInAMonthList = new ArrayList<>();

                for (int k = 0; k < monthRows - 1; k++) {
                    for (int m = 0; m < monthColumns; m++) {
                        String day = getDaysInAMonth(year, monthCount, count++, firstDay, lastDay);

                        daysInThisMonth = new MonthlyLayout(width, height, border, monthRows, monthColumns, yearColumns, LAYOUT, year, monthCount).createLayout(day, firstDay, count);

                        pane.getChildren().add(daysInThisMonth);
                        if (!day.isEmpty()) {
                            eachDayInAMonthList.add(daysInThisMonth);
                        }
                    }
                }
                daysInAMonthList.put(box, eachDayInAMonthList);
                pane.setId("MonthEachPane");
                centerPane.getChildren().add(pane);
            }
        }
        setMonthList(monthsList);
    }

    public List<Node> getMonthList() {
        return yearPaneBoxes;
    }

    private void setMonthList(List<Node> list) {
        yearPaneBoxes = list;
    }

    public Map<HBox, List<VBox>> getFullYearList() {
        return daysInAMonthList;
    }
}
