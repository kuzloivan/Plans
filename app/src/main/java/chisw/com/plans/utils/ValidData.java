package chisw.com.plans.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chisw.com.plans.R;

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

    public static boolean isCredntialsValid(String arg, String pattern)
    {
        return Pattern.matches(pattern, arg);
    }
}
