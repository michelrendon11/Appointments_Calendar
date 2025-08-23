package org.example.Appointments_Calendar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Appointments_Calendar.CalendarView.DayView;
import org.example.Appointments_Calendar.CalendarView.MonthView;
import org.example.Appointments_Calendar.CalendarView.WeekView;
import org.example.Appointments_Calendar.CalendarView.YearView;
import org.example.Appointments_Calendar.HomePage.HomePageButtons;
import org.example.Appointments_Calendar.HomePage.HomeTopPane;
import org.example.Appointments_Calendar.HomePage.TitlePane;
import org.example.Appointments_Calendar.Resorces.Months;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Main Controller class for the Home Page
 **/

public class HomePageController implements Initializable {

    private static final BooleanProperty updateDayView = new SimpleBooleanProperty(false);
    private static boolean isOpen = false;
    private static int count = 0;
    private final List<Months> monthEnum = Arrays.asList(Months.values());

    //Set the default start end time for appointments
    private final int startTime = 9, endTime = 17;
    List<HBox> appointmentList;

    @FXML
    private AnchorPane pane;//main window

    private FlowPane centerPane;//main center pane
    private HBox titlePane, topPane;

    //top buttons for navigation
    private Button yearButton, monthButton, lessMonthButton, plusMonthButton, lessYearButton, pluYearButton, lessWeekButton, weekButton, plusWeekButton, lessDayButton, dayButton, plusDayButton;

    private ZonedDateTime zdt;
    private double width, height, start;//window dimensions for layout
    private int year, month, day, yearToday, monthToday, dayToday;
    private String layout, monthString;

    //update day view layout to refresh appointments
    public static void setUpdateDayView(boolean update) {
        updateDayView.set(update);
    }

    //check if appointment window is open
    public static void setIsNotOpen() {
        isOpen = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        zdt = ZonedDateTime.now();
        year = zdt.getYear();
        month = zdt.getMonthValue();
        monthString = zdt.getMonth().toString();
        day = zdt.getDayOfMonth();
        ZonedDateTime today = ZonedDateTime.now();
        yearToday = today.getYear();
        monthToday = today.getMonthValue();
        dayToday = today.getDayOfMonth();
        layout = "month";
        updateDayView.addListener((_, _, newV) -> {
            if (newV) {
                layout = "day";
                clearChildren();
                try {
                    setUpHomePage();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pane.widthProperty().addListener(_ -> {
            clearChildren();
            try {
                setUpHomePage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        pane.heightProperty().addListener(_ -> {
            clearChildren();
            try {
                setUpHomePage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //begins filling in the main window
    private void setUpHomePage() throws FileNotFoundException {
        titlePane = new TitlePane(pane.getWidth()).getTitlePane();
        HomePageButtons homePageButtons = new HomePageButtons();
        topPane = new HomeTopPane(pane.getWidth(), titlePane.getPrefHeight()).getToPane();

        topPane.getChildren().addAll(
                homePageButtons.getYear(),
                homePageButtons.getMonth(),
                homePageButtons.getWeek(),
                homePageButtons.getDay());

        centerPane = new FlowPane();
        centerPane.setLayoutY(start);
        centerPane.setLayoutX(10);
        pane.getChildren().addAll(titlePane, topPane);
        pane.getChildren().add(centerPane);

        getTopPaneButtons();
        changeViewOfCenterPane();
        listenToTopPaneButtons();
    }

    //clear center pane to display new layout
    private void changeViewOfCenterPane() throws FileNotFoundException {
        width = pane.getWidth();
        height = pane.getHeight() - topPane.getPrefHeight() - titlePane.getPrefHeight();
        start = topPane.getPrefHeight() + titlePane.getPrefHeight();
        centerPane.getChildren().clear();
        centerPane.getChildren().add(addViewToCenterPane(layout));
    }

    //display right layout on center pane: year, month, week, day
    private FlowPane addViewToCenterPane(String s) throws FileNotFoundException {
        if (checkIfIsTodayDay()) {
            dayButton.setId("TodayDayButton");
        } else {
            dayButton.setId("topPaneButtons");
        }
        switch (s) {
            case "year" -> {
                yearButton.setDisable(true);
                YearView yearView = new YearView(width, height, start, year, month, zdt);
                List<Node> yearPaneMonths = yearView.getMonthList();
                Map<HBox, List<VBox>> fullYearList = yearView.getFullYearList();
                listenToMonthPanesInYearView(yearPaneMonths, fullYearList);
                return yearView.getCenterPane();
            }
            case "month" -> {
                monthButton.setDisable(true);
                MonthView monthView = new MonthView(width, height, start, year, month, zdt);
                List<VBox> monthPaneDays = monthView.getEachDayList();
                listenToDayPanesInMonthView(monthPaneDays);
                return monthView.getCenterPane();
            }
            case "week" -> {
                weekButton.setDisable(true);
                WeekView weekView = new WeekView(width, height, start, zdt, startTime, endTime);
                List<VBox> weekDayNamesPanes = weekView.getWeekDaysNamesList();
                List<HBox> weekBoxes = weekView.getWeekBoxes();

                listenToWeekBoxesOnWeekView(weekBoxes, zdt.
                        get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));

                listenToWeekDaysInWeekView(weekDayNamesPanes, zdt.
                        get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));

                return weekView.getCenterPane();
            }
            default -> {
                dayButton.setDisable(true);
                DayView dayView = new DayView(width, height, start, zdt, startTime, endTime);
                appointmentList = dayView.getAppointmentList();
                listenToAppointmentsInDayView(appointmentList);
                setUpdateDayView(false);
                return dayView.getCenterPane();
            }
        }
    }

    //initialize top pane buttons
    private void getTopPaneButtons() {
        lessYearButton = HomePageButtons.getLessYear();
        yearButton = HomePageButtons.getCurrentYear();
        pluYearButton = HomePageButtons.getPlusYear();

        lessMonthButton = HomePageButtons.getLessMonth();
        monthButton = HomePageButtons.getCurrentMonth();
        plusMonthButton = HomePageButtons.getPlusMonth();

        lessWeekButton = HomePageButtons.getLessWeek();
        weekButton = HomePageButtons.getCurrentWeek();
        plusWeekButton = HomePageButtons.getPlusWeek();

        lessDayButton = HomePageButtons.getLessDay();
        dayButton = HomePageButtons.getCurrentDay();
        plusDayButton = HomePageButtons.getPlusDay();

        yearButton.setText(String.valueOf(zdt.getYear()));
        monthString = zdt.getMonth().toString();
        monthButton.setText(switchMonthName(monthString));
        weekButton.setText(String.valueOf(zdt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)));
        dayButton.setText(String.valueOf(zdt.getDayOfMonth()));
    }

    //add listeners to top pane navigation buttons
    private void listenToTopPaneButtons() {
        lessYearButton.setOnMouseClicked(_ -> {
            try {
                subtractYear();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        yearButton.setOnMouseClicked(_ -> {
            try {
                setYearView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        pluYearButton.setOnMouseClicked(_ -> {
            try {
                addYear();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        lessMonthButton.setOnMouseClicked(_ -> {
            try {
                subtractMonth();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        monthButton.setOnMouseClicked(_ -> {
            try {
                setMonthView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        plusMonthButton.setOnMouseClicked(_ -> {
            try {
                addMonth();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        lessWeekButton.setOnMouseClicked(_ -> {
            try {
                subtractWeek();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        weekButton.setOnMouseClicked(_ -> {
            try {
                setWeekView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        plusWeekButton.setOnMouseClicked(_ -> {
            try {
                addWeek();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        lessDayButton.setOnMouseClicked(_ -> {
            try {
                subtractDay();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        dayButton.setOnMouseClicked(_ -> {
            try {
                setDayView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        plusDayButton.setOnMouseClicked(_ -> {
            try {
                addDay();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //set selected year view
    private void subtractYear() throws FileNotFoundException {
        zdt = zdt.minusYears(1);
        updateTopPaneButtonsText();
    }

    private void setYearView() throws FileNotFoundException {
        monthButton.setDisable(false);
        weekButton.setDisable(false);
        dayButton.setDisable(false);
        layout = "year";
        updateTopPaneButtonsText();
    }

    private void addYear() throws FileNotFoundException {
        zdt = zdt.plusYears(1);
        updateTopPaneButtonsText();
    }

    //set selected month view
    private void subtractMonth() throws FileNotFoundException {
        zdt = zdt.minusMonths(1);
        updateTopPaneButtonsText();
    }

    private void setMonthView() throws FileNotFoundException {
        yearButton.setDisable(false);
        weekButton.setDisable(false);
        dayButton.setDisable(false);
        layout = "month";
        updateTopPaneButtonsText();
    }

    private void addMonth() throws FileNotFoundException {
        zdt = zdt.plusMonths(1);
        updateTopPaneButtonsText();
    }

    //set selected week view
    private void subtractWeek() throws FileNotFoundException {
        zdt = zdt.minusWeeks(1);
        day = day - 7;
        updateTopPaneButtonsText();
    }

    private void setWeekView() throws FileNotFoundException {
        yearButton.setDisable(false);
        monthButton.setDisable(false);
        dayButton.setDisable(false);
        layout = "week";
        updateTopPaneButtonsText();
    }

    private void addWeek() throws FileNotFoundException {
        zdt = zdt.plusWeeks(1);
        day = day + 7;
        updateTopPaneButtonsText();
    }

    //set selected day view
    private void subtractDay() throws FileNotFoundException {
        zdt = zdt.minusDays(1);
        updateTopPaneButtonsText();
    }

    private void setDayView() throws FileNotFoundException {
        yearButton.setDisable(false);
        monthButton.setDisable(false);
        weekButton.setDisable(false);
        layout = "day";
        updateTopPaneButtonsText();
    }

    private void addDay() throws FileNotFoundException {
        zdt = zdt.plusDays(1);
        updateTopPaneButtonsText();
    }

    //add listeners to month block on year view
    private void listenToMonthPanesInYearView(
            List<Node> list, Map<HBox, List<VBox>> yearList) {
        list.forEach(c -> c.setOnMouseClicked(_ -> {
            month = list.indexOf(c) + 1;
            monthButton.setText(switchMonthName(month - 1));

            zdt = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneId.from(zdt));

            layout = "month";
            yearButton.setDisable(false);
            try {
                updateTopPaneButtonsText();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
        yearList.forEach((k, v) -> v.forEach(c -> c.setOnMouseClicked(_ -> {
            Text text = (Text) k.getChildren().getFirst();
            Text text1 = (Text) c.getChildren().getFirst();
            month = switchMonthNumber(text.getText());
            monthButton.setText(switchMonthName(month - 1));
            day = Integer.parseInt(text1.getText());

            zdt = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.from(zdt));

            layout = "day";
            yearButton.setDisable(false);
            try {
                updateTopPaneButtonsText();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        })));
    }

    //add listeners to day block on month view
    private void listenToDayPanesInMonthView(List<VBox> list) {
        list.forEach(c -> c.setOnMouseClicked(_ -> {
            Text text = (Text) c.getChildren().getFirst();
            day = Integer.parseInt(text.getText());

            zdt = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.from(zdt));

            layout = "day";
            monthButton.setDisable(false);
            try {
                updateTopPaneButtonsText();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    //add listeners to day block on week view
    private void listenToWeekDaysInWeekView(List<VBox> list, int week) {
        list.forEach(c -> c.setOnMouseClicked(_ -> setUpNewDayView(list, c, week, true)));
    }

    //add listeners to week block on week view
    private void listenToWeekBoxesOnWeekView(List<HBox> list, int week) {
        list.forEach(c -> {
            c.setOnMouseClicked(_ -> setUpNewDayView(list, c, week, false));
            c.getChildren().getFirst().setOnMouseClicked(_ -> setUpNewDayView(list, c, week, false));
        });
    }

    //set up new day view
    private void setUpNewDayView(List<? extends Node> list, Node box, int week, boolean topPane) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int day = topPane ? list.indexOf(box) + 1 : switchDayOfWeek(list.indexOf(box) + 1);

        zdt = ZonedDateTime.now().withYear(year).with(weekFields.weekOfYear(), week).with(weekFields.dayOfWeek(), day);

        layout = "day";
        weekButton.setDisable(false);
        try {
            updateTopPaneButtonsText();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //add listeners to day block on day view
    private void listenToAppointmentsInDayView(List<HBox> list) {
        list.forEach(c -> {
            c.setOnMouseClicked(_ -> setupNewAppointmentView(list, c));
            HBox box = (HBox) c.getChildren().getLast();
            box.getChildren().getFirst().setOnMouseClicked(_ -> setupNewAppointmentView(list, c));
        });
    }

    //set up new appointment view
    private void setupNewAppointmentView(List<HBox> list, HBox box) {
        AppointmentController.setAppointmentList(appointmentList);
        AppointmentController.setZdt(zdt);
        AppointmentController.setAppointmentTime(box);
        if (!isOpen) {
            try {
                new AppointmentApplication().start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            isOpen = true;
        }
        list.forEach(e -> e.setDisable(true));
    }

    //update top pane buttons navigation text with proper year, month, week, day
    private void updateTopPaneButtonsText() throws FileNotFoundException {
        year = zdt.getYear();
        yearButton.setText(String.valueOf(year));
        month = zdt.getMonthValue();
        monthString = zdt.getMonth().toString();
        monthButton.setText(switchMonthName(monthString));
        weekButton.setText(String.valueOf(zdt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)));
        day = zdt.getDayOfMonth();
        dayButton.setText(String.valueOf(day));
        changeViewOfCenterPane();
    }

    private void clearChildren() {
        pane.getChildren().clear();
    }

    //sets a three letter month name from a full month name
    private String switchMonthName(String month) {
        return month.charAt(0) + month.substring(1, 3).toLowerCase();
    }

    //gets full name month from an integer
    private String switchMonthName(int month) {
        return switchMonthName(monthEnum.get(month).toString());
    }

    //check if the day is today
    private boolean checkIfIsTodayDay() {
        return zdt.getYear() == yearToday && zdt.getMonthValue() == monthToday && zdt.getDayOfMonth() == dayToday;
    }

    //help to replace month number from 0-11 to 1-12
    private int switchMonthNumber(String month) {
        return switch (month.toLowerCase()) {
            case "january" -> 1;
            case "february" -> 2;
            case "march" -> 3;
            case "april" -> 4;
            case "may" -> 5;
            case "june" -> 6;
            case "july" -> 7;
            case "august" -> 8;
            case "september" -> 9;
            case "october" -> 10;
            case "november" -> 11;
            default -> 12;
        };
    }

    //help to get the right week number in the year
    private int switchDayOfWeek(int n) {
        if (n <= 7) {
            return n;
        } else if (n <= 14) {
            return n - 7;
        } else if (n <= 21) {
            return n - 14;
        } else if (n <= 28) {
            return n - 21;
        } else if (n <= 35) {
            return n - 28;
        } else if (n <= 42) {
            return n - 35;
        } else if (n <= 49) {
            return n - 42;
        } else if (n <= 56) {
            return n - 49;
        } else if (n <= 63) {
            return n - 56;
        } else if (n <= 70) {
            return n - 63;
        } else if (n <= 77) {
            return n - 70;
        } else if (n <= 84) {
            return n - 77;
        } else if (n <= 91) {
            return n - 84;
        } else if (n <= 98) {
            return n - 91;
        } else if (n <= 105) {
            return n - 98;
        } else if (n <= 112) {
            return n - 105;
        } else {
            return n - 112;
        }
    }
}
