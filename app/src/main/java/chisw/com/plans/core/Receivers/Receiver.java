package chisw.com.plans.core.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.utils.SystemUtils;


/**
 * Created by Yuriy on 16.06.2015.
 */
public class Receiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        PendingIntent pIntent1 = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        int id = Integer.parseInt(intent.getAction());
        sendNotif(id, pIntent1, ctx);
        Multimedia multimedia = ((PApplication) ctx.getApplicationContext()).getMultimedia();
        String path = ((PApplication) ctx.getApplicationContext()).getDbManager().getAudioPathByID(id);
        multimedia.alarmNontification(path);
    }

    void sendNotif(int id, PendingIntent pIntent, Context ctx) {
        if (SystemUtils.isICSHigher()) {
            Intent notificationIntent = new Intent();
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = ctx.getResources();
            Notification.Builder builder = new Notification.Builder(ctx);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id))
                    .setContentText("Wake up !!!");
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        } else {
            NotificationManager nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
            Notification notif = new Notification(R.drawable.ic_alarm, "Wake up !!!", System.currentTimeMillis());
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.setLatestEventInfo(ctx, ((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id), "Wake up !!!", pIntent);
            nm.notify(id, notif);
        }
    }
}