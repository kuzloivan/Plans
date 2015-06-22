package chisw.com.plans.core.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.SharedHelper;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.PlannerActivity;
import chisw.com.plans.ui.activities.SettingsActivity;
import chisw.com.plans.utils.SystemUtils;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final int TEST_ID = 1;

    PApplication app;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        PendingIntent pIntent1 = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        int id = Integer.parseInt(intent.getAction());
        if (app.getSharedHelper().getNotificationOn()) {
            sendNotif(id, pIntent1, ctx);
        }
        app = (PApplication) ctx.getApplicationContext();

    }

    void sendNotif(int id, PendingIntent pIntent, Context ctx) {
        if (SystemUtils.isICSHigher()) {
            Intent notificationIntent = new Intent(ctx, PlannerActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, TEST_ID + id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(ctx);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(app.getDbManager().getTitleByID(id))
                    .setContentText("Wake up !!!");
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


            try {
                Multimedia multimedia = (app.getMultimedia());
                String path = (app.getDbManager().getAudioPathByID(id));
                multimedia.alarmNontification(path);
            } catch (Exception e) {
                Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notification.sound = ringURI;
            }
            if (app.getSharedHelper().getVibrationOn()) {


                if (app.getSharedHelper().getVibrationOn()) {
                    long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
                    notification.vibrate = vibrate;
                }


                notificationManager.notify(NOTIFY_ID + id, notification);
            } else {
                NotificationManager nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
                Notification notif = new Notification(R.drawable.ic_alarm, "Wake up !!!", System.currentTimeMillis());
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.setLatestEventInfo(ctx, app.getDbManager().getTitleByID(id), "Wake up !!!", pIntent);
                nm.notify(NOTIFY_ID + id, notif);
            }
        }
    }
}