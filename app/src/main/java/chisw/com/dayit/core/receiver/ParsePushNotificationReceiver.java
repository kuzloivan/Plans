package chisw.com.dayit.core.receiver;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.parse.ParseBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.core.SharedHelper;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.db.DBManager;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.net.NetManager;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;

/**
 * Created by vdboo_000 on 08.07.2015.
 */
public class ParsePushNotificationReceiver extends ParseBroadcastReceiver {
    private String mTitle;
    private String mDetails;
    private Long time;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        SharedHelper sharedHelper = ((PApplication) context.getApplicationContext()).getSharedHelper();
        DBManager dbManager = ((PApplication) context.getApplicationContext()).getDbManager();
        NetManager netManager = ((PApplication) context.getApplicationContext()).getNetManager();
        chisw.com.dayit.db.AlarmManager alarmManager = ((PApplication) context.getApplicationContext()).getAlarmManager();

        try {
            Bundle extras = intent.getExtras();
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jObj = new JSONObject(jsonData);
            mTitle = jObj.getString("alert");
            mDetails = jObj.getString("title");
            time = jObj.getLong("time");
        } catch (JSONException ex) {
            return;
        }

        if (SystemUtils.isJellyBeanHigher()) {
            sendNotificationForJBAndHigher(context, sharedHelper.getVibrationOn());
        } else {
            sendNotificationForICSAndLower(context, sharedHelper.getVibrationOn());
        }

        setPlanToDB(context, dbManager, netManager, sharedHelper.getSynchronization());
        setPlanToExecute(dbManager, alarmManager, context);
    }

    private void sendNotificationForJBAndHigher(Context pContext, boolean pVibration) {
        Notification.Builder builder = new Notification.Builder(pContext);
        builder.setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(mTitle)
                .setContentText(mDetails);
        Notification notification = builder.build();
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (pVibration) {
            notification.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        }
        notificationManager.notify(101 + (int) System.currentTimeMillis(), notification);
    }

    private void sendNotificationForICSAndLower(Context pContext, boolean pVibration) {
        Intent stopMusicIntent = new Intent(pContext, NotificationCancelReceiver.class);
        stopMusicIntent.setAction("notification_cancelled");
        PendingIntent stopMusicPIntent = PendingIntent.getBroadcast(pContext, 0, stopMusicIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) pContext.getSystemService(pContext.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_alarm, mDetails, time);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(pContext, mTitle, mDetails, stopMusicPIntent);

        if (pVibration) {
            notif.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        }
        nm.notify(101 + (int) System.currentTimeMillis(), notif);
    }

    private void setPlanToDB(Context context, final DBManager pDBManager, NetManager pNetManager, boolean pSynchronization) {
        final Plan p = new Plan();
        p.setTitle(mTitle);
        p.setDetails(mDetails);
        p.setTimeStamp(time);
        p.setDaysToAlarm("0000000");
        p.setIsDeleted(0);
        if (!pSynchronization || !SystemUtils.checkNetworkStatus(context)) {
            p.setIsSynchronized(0);
            pDBManager.saveNewPlan(p);
            return;
        }
        p.setIsSynchronized(1);
        pDBManager.saveNewPlan(p);
        pNetManager.addPlan(p, new OnSaveCallback() {
            @Override
            public void getId(String id) {

                p.setParseId(id);
                int planId = pDBManager.getPlanById(pDBManager.getLastPlanID()).getLocalId();
                p.setLocalId(planId);
                pDBManager.editPlan(p, planId);
            }
        });
    }

    private void setPlanToExecute(DBManager pDBManager, chisw.com.dayit.db.AlarmManager pAlarmManager, Context pContext) {
        int pendingId = pDBManager.getLastPlanID();
        PendingIntent pendingIntent = pAlarmManager.createPendingIntent(Integer.toString(pendingId));
        ((AlarmManager) pContext.getSystemService(pContext.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
