package chisw.com.dayit.utils;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class DataUtils {

    public static String getDateStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(timeStamp);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("HH:mm").format(timeStamp);
    }

    public static String getTimeStrFromCalendar(Calendar pCalendar)
    {
        Formatter formatter = new Formatter();

        formatter.format("Time: %tH:%tM", pCalendar, pCalendar);

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

        formatter.format("Date: %td/%tm/%tY", pCalendar, pCalendar, pCalendar);

        return formatter.toString();
    }

    public static Calendar getCalendarByTimeStamp(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return  calendar;
    }

    public static String getDaysForRepeatingFromString(String pDaysToRepeat){
        pDaysToRepeat = pDaysToRepeat.substring(1);
        String resStr = "";
        for (int i = 0; i < 7; i++){
            resStr += pDaysToRepeat.charAt(i) == '1' ? (getDayByItsNumber(i) + "/")  : "";
        }
        if(resStr.length() > 0) {
            resStr = resStr.substring(0, resStr.length() - 1);
        }
        else {
            resStr = "No days for repeating";
        }
        return resStr;
    }

    private static String getDayByItsNumber(int number) {
        String day = "";

        switch (number) {
            case 0: day = "Sun";
                break;
            case 1: day = "Mon";
                break;
            case 2: day = "Tue";
                break;
            case 3: day = "Wed";
                break;
            case 4: day = "Thurs";
                break;
            case 5: day = "Fri";
                break;
            case 6: day = "Sat";
                break;
        }
        return day;
    }
}
