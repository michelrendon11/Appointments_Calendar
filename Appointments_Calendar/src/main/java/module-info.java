module org.example.Appointments_Calendar {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires java.sql;
    requires javafx.graphics;


    opens org.example.Appointments_Calendar to javafx.fxml;
    exports org.example.Appointments_Calendar;
    exports org.example.Appointments_Calendar.HomePage;
    opens org.example.Appointments_Calendar.HomePage to javafx.fxml;
    exports org.example.Appointments_Calendar.CalendarView;
    opens org.example.Appointments_Calendar.CalendarView to javafx.fxml;
    exports org.example.Appointments_Calendar.Resorces;
    opens org.example.Appointments_Calendar.Resorces to javafx.fxml;
}