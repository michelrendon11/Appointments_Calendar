package org.example.Appointments_Calendar.AppointmentHandler;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * main class for creating and handling appointments
 */

public class AppointmentHandler {

    private static final Path path = Path.of("Appointments_Calendar/src/main/resources/org/example/Appointments_Calendar/AppointmentsList.txt");
    private static final File file = new File(path.toString());

    public static void writeToFile(String line) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        String[] s = line.split(",".trim());
        if (!checkIfExist(s, true)) writer.write(line + "\n");
        writer.close();
    }

    private static boolean checkIfExist(String[] line, boolean withTime) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String[] s = scanner.nextLine().split(",".trim());

            if (withTime && Objects.equals(line[0], s[0]) && Objects.equals(line[1], s[1]) && Objects.equals(line[2], s[2]) && Objects.equals(line[3], s[3])) {

                return true;

            } else if (!withTime && Objects.equals(line[0], s[0]) && Objects.equals(line[1], s[1]) && Objects.equals(line[2], s[2])) {

                return true;
            }
        }
        return false;
    }

    public static boolean checkIfExist(String year, String month, String day, boolean withTime) throws FileNotFoundException {
        String[] line = new String[]{
                year, switchMonth(month), switchDay(day)
        };
        return checkIfExist(line, withTime);
    }

    public static boolean checkIfExist(String year, String month, String day, String time, boolean withTime) throws FileNotFoundException {
        String[] line = new String[]{
                year, switchMonth(month), switchDay(day), time
        };
        return checkIfExist(line, withTime);
    }

    private static String switchMonth(String month) {
        return switch (month) {
            case "1" -> "Jan";
            case "2" -> "Feb";
            case "3" -> "Mar";
            case "4" -> "Abr";
            case "5" -> "May";
            case "6" -> "Jun";
            case "7" -> "Jul";
            case "8" -> "Aug";
            case "9" -> "Sep";
            case "10" -> "Oct";
            case "11" -> "Nov";
            case "12" -> "Dec";
            default -> throw new IllegalStateException("Unexpected value:" + month);
        };
    }

    private static String switchDay(String day) {
        if (day.isEmpty()) {
            return day;
        } else {
            return Integer.parseInt(day) < 10 ? "0" + day : day;
        }
    }

    public static String getAppointment(String year, String month, String day, String time) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<String> app = new ArrayList<>();
        while (scanner.hasNext()) {
            String[] s = scanner.nextLine().split(",".trim());

            if (Objects.equals(s[0], year) && Objects.equals(s[1], switchMonth(month)) && Objects.equals(s[2], switchDay(day)) && Objects.equals(s[3], time)) {

                app.addAll(Arrays.asList(s).subList(4, s.length));
            }
        }
        if (app.isEmpty()) {
            return "";
        } else {
            StringBuilder s = new StringBuilder();
            for (var r : app) {
                s.append(r.trim()).append(", ");
            }
            return s.substring(0, s.length() - 2).trim();
        }
    }

    public static File getFile() {
        return file;
    }
}
