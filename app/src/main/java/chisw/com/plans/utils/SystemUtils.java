package chisw.com.plans.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.TelecomManager;

public class SystemUtils {
    public static boolean checkNetworkStatus(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        NetworkInfo wifi = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobile = connectivityManager.getActiveNetworkInfo();

        return wifi.isConnected() || mobile.isConnected();
    }
}
