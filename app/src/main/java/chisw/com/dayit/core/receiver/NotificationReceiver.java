package chisw.com.dayit.core.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.others.Multimedia;
import chisw.com.dayit.ui.activities.PlannerActivity;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

/**
 * Created by Yuriy on 16.06.2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final int TEST_ID = 1;

    @Override
    public void onReceive(Context pContext, Intent intent) {
        int id = Integer.parseInt(intent.getAction());

        String daysOfWeekStr = (((PApplication) pContext.getApplicationContext()).getDbManager().getDaysToAlarmById(id));

        if(daysOfWeekStr.indexOf('1') == -1){  // if there are no days to alarm - > return
           return;
        }

        if ((((PApplication) pContext.getApplicationContext()).getSharedHelper().getNotificationOn())
                && ValidData.isDayToAlarmValid(daysOfWeekStr)) {
            sendNotification(id, pContext);
        }
    }
    
    private void sendNotification(int id, Context pContext) {
        Notification notification;
        Intent openPlannerIntent = new Intent(pContext, PlannerActivity.class);
        openPlannerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPlannerPendingIntent = PendingIntent.getActivity(pContext, TEST_ID + id, openPlannerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopMusicIntent = new Intent(pContext , NotificationCancelReceiver.class);
        stopMusicIntent.setAction("notification_cancelled");
        PendingIntent stopMusicPIntent = PendingIntent.getBroadcast(pContext, 0, stopMusicIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(pContext);
        builder .setContentIntent(openPlannerPendingIntent)
                .setDeleteIntent(stopMusicPIntent)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(((PApplication) pContext.getApplicationContext()).getDbManager().getTitleByID(id))
                .setContentText(((PApplication) pContext.getApplicationContext()).getDbManager().getDetailsByID(id));
        if(SystemUtils.isJellyBeanHigher()) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        NotificationManager notificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);


        try {
            Multimedia multimedia = (((PApplication) pContext.getApplicationContext()).getMultimedia());
            String path = (((PApplication) pContext.getApplicationContext()).getDbManager().getAudioPathByID(id));
            multimedia.setPlayTime((((PApplication) pContext.getApplicationContext()).getDbManager().getAudioDurationByID(id)));
            multimedia.alarmNotification(path);
        } catch (Exception e) {
            Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.sound = ringURI;
        }

        if (((PApplication) pContext.getApplicationContext()).getSharedHelper().getVibrationOn()) {
            long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
            notification.vibrate = vibrate;
            Toast.makeText(pContext, "vibrating", Toast.LENGTH_SHORT).show();
        }

        notificationManager.notify(NOTIFY_ID + id, notification);
    }
}