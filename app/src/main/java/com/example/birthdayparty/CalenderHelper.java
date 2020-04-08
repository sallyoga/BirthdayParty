package com.example.birthdayparty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderHelper {

    public static int getNextMonth(Date currentDate) {

        int nextMonth;
        int month = getMonth(currentDate);
        if (month == Calendar.DECEMBER) { // DECEMBER
            nextMonth = Calendar.JANUARY;  // JANUARY;
        } else {
            nextMonth = month + 1;
        }
        return nextMonth;
    }

    public static int getYear(Date currentDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int year;

        if (getNextMonth(currentDate) == Calendar.JANUARY) {
            year = Integer.parseInt(dateFormat.format(currentDate)) + 1;
        } else {
            year = Integer.parseInt(dateFormat.format(currentDate));
        }
        return year;
    }

    public static Date getPreviousDay(long timestamp) {
        return new Date(timestamp - 24 * 60 * 60);
    }

    public static long getDaysBetween(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int getMonth(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        return calendar.get(Calendar.MONTH);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
//        return Integer.parseInt(dateFormat.format(currentDate));
    }
}
