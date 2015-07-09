package chisw.com.dayit.core.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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
import chisw.com.dayit.utils.ValidData;

/**
 * Created by vdboo_000 on 08.07.2015.
 */
public class ParsePushNotificationReceiver extends ParseBroadcastReceiver {
    private String mTitle;
    private String mDetails;
    private Long time;
    private SharedHelper mSharedHelper;
    private DBManager mDBManager;
    private NetManager mNetManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        mSharedHelper = ((PApplication) context.getApplicationContext()).getSharedHelper();
        mDBManager = ((PApplication) context.getApplicationContext()).getDbManager();
        mNetManager = ((PApplication) context.getApplicationContext()).getNetManager();

        try {
            Bundle extras = intent.getExtras();
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jObj = new JSONObject(jsonData);
            mTitle = jObj.getString("alert");
            mDetails = jObj.getString("mTitle");
            time = jObj.getLong("time");
        } catch (JSONException ex) {
            return;
        }

        if (SystemUtils.isJellyBeanHigher()) {
            sendNotificationForJBAndHigher(context);
        } else {
            sendNotificationForICSAndLower(context);
        }

        setPlanToDB(context);
    }

    private void sendNotificationForJBAndHigher(Context pContext) {
        Notification.Builder builder = new Notification.Builder(pContext);
        builder.setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(mTitle)
                .setContentText(mDetails);
        Notification notification = builder.build();
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mSharedHelper.getVibrationOn()) {
            notification.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        }
        notificationManager.notify(101 + (int) System.currentTimeMillis(), notification);
    }

    private void sendNotificationForICSAndLower(Context pContext) {
        Intent stopMusicIntent = new Intent(pContext, NotificationCancelReceiver.class);
        stopMusicIntent.setAction("notification_cancelled");
        PendingIntent stopMusicPIntent = PendingIntent.getBroadcast(pContext, 0, stopMusicIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) pContext.getSystemService(pContext.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_alarm, mDetails, time);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(pContext, mTitle, mDetails, stopMusicPIntent);

        if (mSharedHelper.getVibrationOn()) {
            notif.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        }
        nm.notify(101 + (int) System.currentTimeMillis(), notif);
    }

    public void setPlanToDB(Context context) {
        final Plan p = new Plan();
        p.setDetails(mTitle);
        p.setTitle(mDetails);
        p.setTimeStamp(time);
        p.setIsDeleted(0);

        if (!mSharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(context)) {
            p.setIsSynchronized(0);
            mDBManager.saveNewPlan(p);
            return;
        }
        p.setIsSynchronized(1);
        mDBManager.saveNewPlan(p);
        mNetManager.addPlan(p, new OnSaveCallback() {
            @Override
            public void getId(String id) {
                p.setParseId(id);
                int planId = mDBManager.getPlanById(mDBManager.getLastPlanID()).getLocalId();
                p.setLocalId(planId);
                mDBManager.editPlan(p, planId);
            }
        });
    }
}
