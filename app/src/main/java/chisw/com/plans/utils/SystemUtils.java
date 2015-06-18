package chisw.com.plans.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telecom.TelecomManager;

public class SystemUtils {

    private static final int KITKAT_VERSION = 19;
    private static final int ICS_VERSION = 16;
    public boolean checkNetworkStatus(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        NetworkInfo wifi = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobile = connectivityManager.getActiveNetworkInfo();

        return wifi.isConnected() || mobile.isConnected();
    }

    public static boolean isKitKatHigher(){
        return Build.VERSION.SDK_INT >= KITKAT_VERSION;
    }

    public static boolean isICSHigher(){
        return Build.VERSION.SDK_INT >= ICS_VERSION;
    }

}
