package org.example.Appointments_Calendar.CalendarView;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * helper class to set the right week days numbers for month, year, and week views
 */

public class WeekDaysNumbers {

    static ZonedDateTime zdt, s;

    private static int count, year, weekDay, monthDay;

    public WeekDaysNumbers(ZonedDateTime zdt) {
        WeekDaysNumbers.zdt = zdt;
        year = zdt.getYear();
        weekDay = zdt.getDayOfWeek().getValue();
        monthDay = zdt.getDayOfMonth();
    }

    protected static String getDayNumbers() {
        int day = zdt.minusDays(Math.abs(weekDay)).getDayOfMonth();
        monthDay = zdt.minusDays(Math.abs(weekDay)).getMonthValue();

        s = ZonedDateTime.of(year, monthDay, day,0, 0, 0, 0,ZoneId.from(zdt));

        int dayCount = s.plusDays(count++).getDayOfMonth();
        return String.valueOf(dayCount);
    }

    protected static void startCount() {
        count = 0;
    }
}
