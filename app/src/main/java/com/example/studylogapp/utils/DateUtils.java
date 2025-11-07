package com.example.studylogapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault());
    private static final SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat("yyyy년 M월", Locale.getDefault());

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatDate(Calendar calendar) {
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static String formatDisplayDate(Date date) {
        return DISPLAY_FORMAT.format(date);
    }

    public static String formatDisplayDate(Calendar calendar) {
        return DISPLAY_FORMAT.format(calendar.getTime());
    }

    public static String formatMonthYear(Calendar calendar) {
        return MONTH_YEAR_FORMAT.format(calendar.getTime());
    }

    public static Calendar parseDate(String dateString) {
        try {
            Date date = DATE_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            return Calendar.getInstance();
        }
    }

    public static boolean isToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
               calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static Calendar getFirstDayOfMonth(Calendar calendar) {
        Calendar firstDay = (Calendar) calendar.clone();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        return firstDay;
    }

    public static int getDaysInMonth(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getFirstDayOfWeek(Calendar calendar) {
        Calendar firstDay = getFirstDayOfMonth(calendar);
        int dayOfWeek = firstDay.get(Calendar.DAY_OF_WEEK);
        // 일요일을 0으로 변환
        return (dayOfWeek + 5) % 7;
    }
}


