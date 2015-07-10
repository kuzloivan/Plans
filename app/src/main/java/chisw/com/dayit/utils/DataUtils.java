package chisw.com.dayit.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class DataUtils {

    public static String getDateStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("dd.MM.yyyy").format(timeStamp);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("HH:mm").format(timeStamp);
    }

    public static String getTimeStrFromCalendar(Calendar pCalendar)
    {
        Formatter formatter = new Formatter();

        formatter.format("%tH:%tM", pCalendar, pCalendar);

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

    public static String getDateStrFromCalendar(Calendar pCalendar)
    {
        Formatter formatter = new Formatter();

        formatter.format("%td/%tm/%tY", pCalendar, pCalendar, pCalendar);

        return formatter.toString();
    }

    public static Calendar getCalendarByTimeStamp(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return  calendar;
    }

}
