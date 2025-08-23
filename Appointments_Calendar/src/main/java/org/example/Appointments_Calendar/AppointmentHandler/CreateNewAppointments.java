package org.example.Appointments_Calendar.AppointmentHandler;

/**
 * helper class to create new appointments and save them on file
 */
public class CreateNewAppointments {

    private final String name, phone, email, service, year, month, day, time;

    public CreateNewAppointments(
            String year, String month, String day, String time,
            String name, String phone, String email, String service) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.service = service;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }

    @Override
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s,%s".formatted(
                year, month, day, time, name, phone, email, service);
    }
}
