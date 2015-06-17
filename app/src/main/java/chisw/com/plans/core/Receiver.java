package chisw.com.plans.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import chisw.com.plans.R;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class Receiver extends BroadcastReceiver {
    final String LOG_TAG = "myLogs";
    PendingIntent pIntent1;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(LOG_TAG, "onReceive");
        Log.d(LOG_TAG, "action = " + intent.getAction());
        Log.d(LOG_TAG, "extra = " + intent.getStringExtra("extra"));

        pIntent1 = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        sendNotif(1, pIntent1, ctx);
        Multimedia multimedia = ((PApplication)ctx.getApplicationContext()).getMultimedia();
        multimedia.alarmNontification();
    }

    void sendNotif(int id, PendingIntent pIntent, Context ctx) {

        // todo create notification by Notification.Builder http://developer.android.com/training/notify-user/build-notification.html
        NotificationManager nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_alarm, "Wake up !!!"
                , System.currentTimeMillis());
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(ctx, "Alarm " + id, "Wake up !!!", pIntent);
        nm.notify(id, notif);
    }
}