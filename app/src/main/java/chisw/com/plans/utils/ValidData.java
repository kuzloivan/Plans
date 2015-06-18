package chisw.com.plans.utils;

import android.text.TextUtils;

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
}
