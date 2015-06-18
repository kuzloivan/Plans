package chisw.com.plans.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telecom.TelecomManager;

public class SystemUtils {
    public boolean checkNetworkStatus(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        NetworkInfo wifi = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobile = connectivityManager.getActiveNetworkInfo();

        return wifi.isConnected() || mobile.isConnected();
    }

    public static boolean isKitKatHigher(){
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean isICSHigher(){
        return Build.VERSION.SDK_INT >= 16;
    }

}
