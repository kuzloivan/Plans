package chisw.com.plans.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtils {

    public static String getDateStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("dd.MM.yyyy").format(timeStamp);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        return new SimpleDateFormat("HH:mm").format(timeStamp);
    }
}
