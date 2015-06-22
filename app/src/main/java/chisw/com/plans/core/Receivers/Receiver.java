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
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.PlannerActivity;
import chisw.com.plans.ui.activities.SettingsActivity;
import chisw.com.plans.utils.SystemUtils;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class Receiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final int TEST_ID = 1;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        PendingIntent pIntent1 = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        int id = Integer.parseInt(intent.getAction());
        sendNotif(id, pIntent1, ctx);

    }

    void sendNotif(int id, PendingIntent pIntent, Context ctx) {
        if (SystemUtils.isICSHigher()) {
            Intent notificationIntent = new Intent(ctx, PlannerActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, TEST_ID+id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(ctx);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id))
                    .setContentText("Wake up !!!");
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


            try{
                Multimedia multimedia = ((PApplication) ctx.getApplicationContext()).getMultimedia();
                String path = ((PApplication) ctx.getApplicationContext()).getDbManager().getAudioPathByID(id);
                multimedia.alarmNontification(path);
            }
            catch(Exception e) {
                Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notification.sound = ringURI;
            }

            if(SettingsActivity.Choose_notif_vibration){
                long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
                notification.vibrate = vibrate;
            }


            notificationManager.notify(NOTIFY_ID+id, notification);
        } else {
            NotificationManager nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
            Notification notif = new Notification(R.drawable.ic_alarm, "Wake up !!!", System.currentTimeMillis());
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.setLatestEventInfo(ctx, ((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id), "Wake up !!!", pIntent);
            nm.notify(NOTIFY_ID+id, notif);
        }
    }
}