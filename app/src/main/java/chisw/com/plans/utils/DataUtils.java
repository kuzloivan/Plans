package chisw.com.plans.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class DataUtils {

    private static Calendar calendar = Calendar.getInstance();

    public static String getDateStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("dd.MM.yyyy").format(timeStamp);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("HH:mm").format(timeStamp);
    }

    public static void initializeCalendar()
    {
        DataUtils.setCalendarSeconds(0);
        DataUtils.setCalendarMinute(DataUtils.getCalendar().get(Calendar.MINUTE) + 1);
    }
    public static void setCalendarYear(int year){
        DataUtils.calendar.set(Calendar.YEAR, year);
    }

    public static void setCalendarMonth(int month){
        DataUtils.calendar.set(Calendar.MONTH, month);
    }

    public static void setCalendarDay(int day){
        DataUtils.calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public static void setCalendarHour(int hour){
        DataUtils.calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    public static void setCalendarMinute(int minute){
        DataUtils.calendar.set(Calendar.MINUTE, minute);
    }

    public static void setCalendarSeconds(int seconds){
        DataUtils.calendar.set(Calendar.SECOND, seconds);
    }

    public static Calendar getCalendar()
    {
        return calendar;
    }

    public static String fillTime()
    {
        Formatter formatter = new Formatter();

        formatter.format("Time: %tH:%tM",DataUtils.getCalendar(), DataUtils.getCalendar());

        return formatter.toString();
    }

    public static String fillDate()
    {
        Formatter formatter = new Formatter();

        formatter.format("Date: %td-%tm-%tY", DataUtils.getCalendar(), DataUtils.getCalendar(), DataUtils.getCalendar());

        return formatter.toString();
    }
}
