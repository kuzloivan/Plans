package chisw.com.dayit.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidData
{
    public static boolean isTextValid(String ... args)
    {
        int len=args.length;
        for(int i = 0; i<len; i++)
        {
            if(TextUtils.isEmpty(args[i]))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmailAddressValid(String pArg) {
        return Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), pArg);
    }

    public static boolean isPhoneNumberValid(String arg, String pattern){
        return Pattern.matches(pattern, arg);
    }

    public static boolean isCredentialsValid(String arg, String pattern){
        return Pattern.matches(pattern, arg);
    }

    public static boolean isDayToAlarmValid(String pDaysToAlarmBundle){
        if (pDaysToAlarmBundle.charAt(0) == '0')
            return true;
        else {
            return (pDaysToAlarmBundle.charAt((Calendar.getInstance().get(Calendar.DAY_OF_WEEK))) == '1');
        }
    }
}
