package chisw.com.plans.utils;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class DataUtils {

    private static Calendar CALENDAR = Calendar.getInstance();

    public static String getDateStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("dd.MM.yyyy").format(timeStamp);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("HH:mm").format(timeStamp);
    }

    public static void initializeCalendar()
    {
        DataUtils.setCalendarSeconds(0);
       // DataUtils.setCalendarMinute(DataUtils.getCalendar().get(Calendar.MINUTE) + 1);
    }
    public static void setCalendarYear(int year){
        DataUtils.CALENDAR.set(Calendar.YEAR, year);
    }

    public static void setCalendarMonth(int month){
        DataUtils.CALENDAR.set(Calendar.MONTH, month);
    }

    public static void setCalendarDay(int day){
        DataUtils.CALENDAR.set(Calendar.DAY_OF_MONTH, day);
    }

    public static void setCalendarHour(int hour){
        DataUtils.CALENDAR.set(Calendar.HOUR_OF_DAY, hour);
    }

    public static void setCalendarMinute(int minute){
        DataUtils.CALENDAR.set(Calendar.MINUTE, minute);
    }

    public static void setCalendarSeconds(int seconds){
        DataUtils.CALENDAR.set(Calendar.SECOND, seconds);
    }

    public static Calendar getCalendar()
    {
        return CALENDAR;
    }

    public static String getTimeStrFromCalendar()
    {
        Formatter formatter = new Formatter();

        formatter.format("%tH:%tM", DataUtils.getCalendar(), DataUtils.getCalendar());

        return formatter.toString();
    }

    public static String getTimeStrFromTimeStamp(int timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, timeStamp);

        Formatter formatter = new Formatter();

        formatter.format("%tM:%tS", calendar, calendar);

        return formatter.toString();
    }

    public static String getDateStrFromCalendar()
    {
        Formatter formatter = new Formatter();

        formatter.format("%td/%tm/%tY", DataUtils.getCalendar(), DataUtils.getCalendar(), DataUtils.getCalendar());

        return formatter.toString();
    }

    public static void setCalendar(Calendar calendar)
    {
        CALENDAR = calendar;
    }

    public static Calendar getCalendarByTimeStamp(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return  calendar;
    }

}
