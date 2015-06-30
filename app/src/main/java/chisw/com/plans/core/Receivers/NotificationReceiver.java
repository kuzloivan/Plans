package chisw.com.plans.core.Receivers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.db.Mapper;
import chisw.com.plans.model.Plan;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.PlannerActivity;
import chisw.com.plans.ui.activities.ViewPlanActivity;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final int TEST_ID = 1;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        int id = Integer.parseInt(intent.getAction());

        String daysOfWeekStr = (((PApplication) ctx.getApplicationContext()).getDbManager().getDaysToAlarmById(id));
       // Bundle daysToAlarmBundle = Mapper.parseStringDaysOfWeek(daysOfWeekStr);

        if ((((PApplication) ctx.getApplicationContext()).getSharedHelper().getNotificationOn())
                && ValidData.isDayToAlarmValid(daysOfWeekStr)) {

            if (SystemUtils.isJellyBeanHigher()) {
                sendNotificationForJellyBeanAndHigher(id, ctx);
            } else {
                sendNotificationForIceCreamSandwichAndLower(id, ctx);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationForJellyBeanAndHigher(int id, Context ctx) {
        Intent openPlannerIntent = new Intent(ctx, PlannerActivity.class);
        openPlannerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPlannerPendingIntent = PendingIntent.getActivity(ctx, TEST_ID + id, openPlannerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopMusicIntent = new Intent(ctx , NotificationCancelReceiver.class);
        stopMusicIntent.setAction("notification_cancelled");
        PendingIntent stopMusicPIntent = PendingIntent.getBroadcast(ctx, 0, stopMusicIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(ctx);
        builder .setContentIntent(openPlannerPendingIntent)
                .setDeleteIntent(stopMusicPIntent)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id))
                .setContentText(((PApplication) ctx.getApplicationContext()).getDbManager().getDetailsByID(id));

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        try {
            Multimedia multimedia = (((PApplication) ctx.getApplicationContext()).getMultimedia());
            String path = (((PApplication) ctx.getApplicationContext()).getDbManager().getAudioPathByID(id));
            multimedia.setPlayTime((((PApplication) ctx.getApplicationContext()).getDbManager().getAudioDurationByID(id)));
            multimedia.alarmNotification(path);
        } catch (Exception e) {
            Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.sound = ringURI;
        }

        if (((PApplication) ctx.getApplicationContext()).getSharedHelper().getVibrationOn()) {
            long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
            notification.vibrate = vibrate;
            Toast.makeText(ctx, "vibrating", Toast.LENGTH_SHORT).show();
        }

        notificationManager.notify(NOTIFY_ID + id, notification);
    }

    private void sendNotificationForIceCreamSandwichAndLower(int id, Context ctx) {
        Intent stopMusicIntent = new Intent(ctx , NotificationCancelReceiver.class);
        stopMusicIntent.setAction("notification_cancelled");
        PendingIntent stopMusicPIntent = PendingIntent.getBroadcast(ctx, 0, stopMusicIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_alarm, ((PApplication) ctx.getApplicationContext()).getDbManager().getDetailsByID(id), System.currentTimeMillis());
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(ctx, ((PApplication) ctx.getApplicationContext()).getDbManager().getTitleByID(id), ((PApplication) ctx.getApplicationContext()).getDbManager().getDetailsByID(id), stopMusicPIntent);

        try {
            Multimedia multimedia = (((PApplication) ctx.getApplicationContext()).getMultimedia());
            String path = (((PApplication) ctx.getApplicationContext()).getDbManager().getAudioPathByID(id));
            multimedia.setPlayTime((((PApplication) ctx.getApplicationContext()).getDbManager().getAudioDurationByID(id)));
            multimedia.alarmNotification(path);
        } catch (Exception e) {
            Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notif.sound = ringURI;
        }

        if (((PApplication) ctx.getApplicationContext()).getSharedHelper().getVibrationOn()) {
            long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
            notif.vibrate = vibrate;
            Toast.makeText(ctx, "vibrating", Toast.LENGTH_SHORT).show();
        }
        nm.notify(NOTIFY_ID + id, notif);
    }


}