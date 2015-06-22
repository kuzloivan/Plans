package chisw.com.plans.core.Receivers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;
import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.PlannerActivity;
import chisw.com.plans.ui.activities.SplashActivity;
import chisw.com.plans.utils.SystemUtils;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class RebootReceiver extends BroadcastReceiver {
    final String LOG_TAG = "myLogs";
    PendingIntent pIntent1;
    private static final int NOTIFY_ID = 101;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(LOG_TAG, "Start");
        Toast.makeText(ctx, "Some Text. Device booting 1", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Toast.makeText(ctx, "Some Text. Device booting 2", Toast.LENGTH_LONG).show();
        }
    }

}