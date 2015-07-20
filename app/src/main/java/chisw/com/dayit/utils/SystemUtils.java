package chisw.com.dayit.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

public class SystemUtils {

    private static final int KITKAT_VERSION = 19;
    private static final int JELLY_BEAN_VERSION = 16;

    public static boolean checkNetworkStatus(Context pContext)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetooth = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        NetworkInfo wimax = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        if (wifi == null && mobile == null && bluetooth == null
                && wimax == null) {
            return false;
        }

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        if (bluetooth != null && bluetooth.isConnected()) {
            return true;
        }

        if (wimax != null && wimax.isConnected()) {
            return true;
        }

        return false;
    }

    public static void hideKeyboard(Activity activity, View view){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isKitKatHigher(){
        return Build.VERSION.SDK_INT >= KITKAT_VERSION;
    }

    public static boolean isJellyBeanHigher(){
        return Build.VERSION.SDK_INT >= JELLY_BEAN_VERSION;
    }

    public static File createDirectory(){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File dayItDir = new File(root + "/DayIt");
        if(!dayItDir.exists()){
            dayItDir.mkdirs();
        }
        return dayItDir;
    }
}
