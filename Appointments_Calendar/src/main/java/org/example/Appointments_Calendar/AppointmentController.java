package org.example.Appointments_Calendar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Appointments_Calendar.AppointmentHandler.AppointmentHandler;
import org.example.Appointments_Calendar.AppointmentHandler.CreateNewAppointments;
import org.example.Appointments_Calendar.Resorces.WeekDays;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * controller for the appointments window
 */

public class AppointmentController implements Initializable {

    static List<WeekDays> weekDays = List.of(WeekDays.values());
    private static HBox appointmentTime;
    private static List<HBox> appointmentList;
    private static ZonedDateTime zdt;
    private static String name, phone, email, service;
    private static boolean editFile = false;
    List<Button> buttons;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane titlePane;
    @FXML
    private HBox titleNamePane;
    @FXML
    private HBox titleClosePane;
    @FXML
    private VBox appointmentPane;
    @FXML
    private Text appointmentViewTitle;
    @FXML
    private Button close;
    @FXML
    private Button create;
    @FXML
    private Button edit;
    @FXML
    private Button cancel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea serviceFiled;
    @FXML
    private Text nameText;
    @FXML
    private Text numberText;
    @FXML
    private Text emailText;
    @FXML
    private Text serviceText;
    @FXML
    private Text timeText;
    @FXML
    private Text dayText;
    @FXML
    private Text weekDayText;
    @FXML
    private Text monthText;
    @FXML
    private Text yearText;
    private double x, y;

    private static String getWeekDay() {
        return String.valueOf(weekDays.get(getDayOfTheWeekNumber(zdt)));
    }

    private static String getMonth() {
        return switchMonthName(zdt.getMonth().toString());
    }

    private static String switchMonthName(String month) {
        return month.charAt(0) + month.substring(1, 3).toLowerCase();
    }

    private static String getYear() {
        return String.valueOf(zdt.getYear());
    }

    private static String getTime() {
        HBox hBox = (HBox) appointmentTime.getChildren().getFirst();
        Text text = (Text) hBox.getChildren().getFirst();
        return text.getText();
    }

    public static void setAppointmentList(List<HBox> list) {
        appointmentList = list;
    }

    public static void setZdt(ZonedDateTime zdt) {
        AppointmentController.zdt = zdt;
    }

    //current fills on appointment window
    public static void setAppointmentTime(HBox box) {
        AppointmentController.appointmentTime = box;
        HBox hBox = (HBox) box.getChildren().getLast();
        TextField text = (TextField) hBox.getChildren().getFirst();
        String[] s = text.getText().split(",".trim());
        if (s.length > 1) {
            setName(s[0].trim());
            setPhone(s[1].trim());
            setEmail(s[2].trim());
            setService(Arrays.toString(Arrays.stream(s).skip(3).toArray()).trim());
        } else {
            setName("");
            setEmail("");
            setPhone("");
            setService("");
        }
    }

    //help to replace the day of the week from 1-7 to 0-6
    private static int getDayOfTheWeekNumber(ZonedDateTime s) {

        int i = ZonedDateTime.of(s.getYear(), s.getMonthValue(), s.getDayOfMonth(), 0, 0, 0, 0, s.getZone()).getDayOfWeek().getValue();

        return i > 6 ? 0 : i;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = Arrays.asList(cancel, close, create, edit);

        appointmentPane.setId("appointmentPane");
        pane.setId("appointmentPane");
        titlePane.setId("titlePane");
        appointmentViewTitle.setId("appointmentViewTitle");

        close.setId("close");
        create.setId("buttons");
        edit.setId("buttons");
        cancel.setId("buttons");

        nameText.setId("texts2");
        numberText.setId("texts2");
        emailText.setId("texts2");
        serviceText.setId("texts1");
        timeText.setId("texts1");
        timeText.setText(getTime());

        nameField.setId("fields");
        phoneField.setId("fields");
        emailField.setId("fields");
        serviceFiled.setId("area");
        nameField.setText(name);
        phoneField.setText(phone);
        emailField.setText(email);
        serviceFiled.setText(service);

        dayText.setId("texts1");
        dayText.setText(getDay());
        weekDayText.setId("texts1");
        weekDayText.setText(getWeekDay());
        monthText.setId("texts1");
        monthText.setText(getMonth());
        yearText.setId("texts1");
        yearText.setText(getYear());
        initialState();
        moveScene();
    }

    //disable all fields is appointment is already created
    private void initialState() {
        if (!nameField.getText().isEmpty()) {
            nameField.setDisable(true);
            phoneField.setDisable(true);
            emailField.setDisable(true);
            serviceFiled.setDisable(true);
            create.setDisable(true);
        } else {
            edit.setDisable(true);
        }
    }

    //cancel and close appointment window
    @FXML
    void cancel() {
        editFile = false;
        close();
    }

    //create new appointment
    @FXML
    void create() throws IOException {
        if (editFile) {
            File file = AppointmentHandler.getFile();
            String line = getYear() + "," + getMonth() + "," + getDay() + "," + getTime();
            removeLine(line, file);
        }

        AppointmentHandler.writeToFile(new CreateNewAppointments(getYear(), getMonth(), getDay(), getTime(), getName(), getPhone(), getEmail(), getService()).toString());

        editFile = false;
        close();
    }

    //edit existing appointment
    @FXML
    void edit() {
        nameField.setDisable(false);
        phoneField.setDisable(false);
        emailField.setDisable(false);
        serviceFiled.setDisable(false);
        create.setDisable(false);
        edit.setDisable(true);
        editFile = true;
    }

    //close appointment window
    @FXML
    void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        HomePageController.setIsNotOpen();
        appointmentList.forEach(c -> c.setDisable(false));
        stage.close();
        HomePageController.setUpdateDayView(true);
    }

    //make window draggable
    private void moveScene() {
        pane.setOnMouseClicked(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getScreenY() - y);
        });
    }

    private String getDay() {
        int day = zdt.getDayOfMonth();
        return day < 10 ? "0" + day : String.valueOf(day);
    }

    private String getName() {
        return nameField.getText().isEmpty() ? "Name" : nameField.getText();
    }

    private static void setName(String s) {
        if (s.length() > 1) {
            name = s;
        } else {
            name = "";
        }
    }

    private String getPhone() {
        return phoneField.getText().isEmpty() ? "( )" : phoneField.getText();
    }

    private static void setPhone(String s) {
        phone = s;
    }

    private String getEmail() {
        return emailField.getText().isEmpty() ? "@" : emailField.getText();
    }

    private static void setEmail(String s) {
        email = s;
    }

    private String getService() {
        return serviceFiled.getText().isEmpty() ? "Standard" : serviceFiled.getText();
    }

    private static void setService(String s) {
        if (s.length() > 1) {
            service = s.substring(1, s.length() - 1).trim();
        } else {
            service = "";
        }
    }

    //update appointment file is an appointment is edited
    public void removeLine(String line, File file) throws IOException {

        List<String> out = Files.lines(file.toPath()).filter(c -> !c.contains(line)).collect(Collectors.toList());

        Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
