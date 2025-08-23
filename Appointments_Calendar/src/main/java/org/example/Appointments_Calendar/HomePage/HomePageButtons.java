package org.example.Appointments_Calendar.HomePage;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the top pane navigation buttons, and provides setters and getters to update them
 */

public class HomePageButtons {

    private static final List<Button> buttons = new ArrayList<>();
    private static Button currentMonth, currentYear, lessMonth,
            plusMonth, plusYear, lessYear, currentWeek, plusWeek,
            lessWeek, lessDay, currentDay, plusDay;
    private final VBox yearBox, monthBox, weekBox, dayBox;

    public HomePageButtons() {
        yearBox = new VBox();
        monthBox = new VBox();
        weekBox = new VBox();
        dayBox = new VBox();

        yearBox.setId("TopPaneNamesBox");
        monthBox.setId("TopPaneNamesBox");
        weekBox.setId("TopPaneNamesBox");
        dayBox.setId("TopPaneNamesBox");

        HBox yearBoxButtons = new HBox();
        HBox monthBoxButtons = new HBox();
        HBox weekBoxButtons = new HBox();
        HBox dayBoxButtons = new HBox();

        setUpButtons();

        yearBoxButtons.setId("yearAndMonthBox");
        monthBoxButtons.setId("yearAndMonthBox");
        weekBoxButtons.setId("yearAndMonthBox");
        dayBoxButtons.setId("yearAndMonthBox");

        yearBoxButtons.getChildren().addAll(getYearButtons());
        monthBoxButtons.getChildren().addAll(getMonthButtons());
        weekBoxButtons.getChildren().addAll(getWeekButtons());
        dayBoxButtons.getChildren().addAll(getDayButtons());

        yearBox.getChildren().addAll(setNames("Year"), yearBoxButtons);
        monthBox.getChildren().addAll(setNames("Month"), monthBoxButtons);
        weekBox.getChildren().addAll(setNames("Week"), weekBoxButtons);
        dayBox.getChildren().addAll(setNames("Day"), dayBoxButtons);
        buttons.forEach(c -> c.setId("topPaneButtons"));
    }

    private static Text setNames(String s) {
        Text text = new Text();
        text.setId("TopPaneNames");
        text.setText(s);
        return text;
    }

    public static Button getLessYear() {
        return lessYear;
    }

    public static Button getCurrentYear() {
        return currentYear;
    }

    public static Button getPlusYear() {
        return plusYear;
    }

    public static Button getLessMonth() {
        return lessMonth;
    }

    public static Button getCurrentMonth() {
        return currentMonth;
    }

    public static Button getPlusMonth() {
        return plusMonth;
    }

    public static Button getLessWeek() {
        return lessWeek;
    }

    public static Button getCurrentWeek() {
        return currentWeek;
    }

    public static Button getPlusWeek() {
        return plusWeek;
    }

    public static Button getLessDay() {
        return lessDay;
    }

    public static Button getCurrentDay() {
        return currentDay;
    }

    public static Button getPlusDay() {
        return plusDay;
    }

    public VBox getYear() {
        return yearBox;
    }

    public VBox getMonth() {
        return monthBox;
    }

    public VBox getWeek() {
        return weekBox;
    }

    public VBox getDay() {
        return dayBox;
    }

    //create top pane navigation buttons
    public void setUpButtons() {
        lessYear = new Button();
        currentYear = new Button();
        plusYear = new Button();
        lessMonth = new Button();
        currentMonth = new Button();
        plusMonth = new Button();
        lessWeek = new Button();
        currentWeek = new Button();
        plusWeek = new Button();
        lessDay = new Button();
        currentDay = new Button();
        plusDay = new Button();

        lessYear.setText("<");
        currentYear.setText("");
        currentYear.setPrefWidth(80);
        plusYear.setText(">");
        lessMonth.setText("<");
        currentMonth.setText("");
        currentMonth.setPrefWidth(80);
        plusMonth.setText(">");
        lessWeek.setText("<");
        currentWeek.setText("");
        currentWeek.setPrefWidth(80);
        plusWeek.setText(">");
        lessDay.setText("<");
        currentDay.setText("");
        currentDay.setPrefWidth(80);
        plusDay.setText(">");
    }

    private List<Button> getYearButtons() {
        List<Button> btn = List.of(lessYear, currentYear, plusYear);
        buttons.addAll(btn);
        return btn;
    }

    private List<Button> getMonthButtons() {
        List<Button> btn = List.of(lessMonth, currentMonth, plusMonth);
        buttons.addAll(btn);
        return btn;
    }

    private List<Button> getWeekButtons() {
        List<Button> btn = List.of(lessWeek, currentWeek, plusWeek);
        buttons.addAll(btn);
        return btn;
    }

    private List<Button> getDayButtons() {
        List<Button> btn = List.of(lessDay, currentDay, plusDay);
        buttons.addAll(btn);
        return btn;
    }
}
