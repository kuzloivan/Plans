package chisw.com.plans.core.Receivers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import chisw.com.plans.R;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.PlannerActivity;
import chisw.com.plans.ui.activities.SplashActivity;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class RebootReceiver extends BroadcastReceiver {
    final String LOG_TAG = "myLogs";
    PendingIntent pIntent1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Start");
        Toast.makeText(context, "Text out if", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Toast.makeText(context, "Text in if", Toast.LENGTH_LONG).show();

        }
    }
}