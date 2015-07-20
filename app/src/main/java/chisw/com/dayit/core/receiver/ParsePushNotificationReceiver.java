package chisw.com.dayit.core.receiver;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.parse.ParseBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.core.SharedHelper;
import chisw.com.dayit.core.callback.OnImageDownloadCompletedCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.db.DBManager;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.net.NetManager;
import chisw.com.dayit.ui.activities.PlannerActivity;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public class ParsePushNotificationReceiver extends ParseBroadcastReceiver {
    private String mTitle;
    private String mDetails;
    private Long mTime;
    private String mFrom;
    private String mParseID;
    private DBManager dbManager;
    private SharedHelper sharedHelper;
    private NetManager netManager;
    private chisw.com.dayit.others.AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        dbManager = ((PApplication) context.getApplicationContext()).getDbManager();
        sharedHelper = ((PApplication) context.getApplicationContext()).getSharedHelper();
        netManager = ((PApplication) context.getApplicationContext()).getNetManager();
        alarmManager = ((PApplication) context.getApplicationContext()).getAlarmManager();

        try {
            Bundle extras = intent.getExtras();
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jObj = new JSONObject(jsonData);
            mTitle = jObj.getString(context.getString(R.string.json_alert));
            mDetails = jObj.getString(context.getString(R.string.json_title));
            mTime = jObj.getLong(context.getString(R.string.json_time));
            mFrom = jObj.getString(context.getString(R.string.json_from));
            if (!jObj.isNull(context.getString(R.string.json_parseId)))
            /*if (jObj.getString(context.getString(R.string.json_parseId)) != null)*/
                mParseID = jObj.getString(context.getString(R.string.json_parseId));
            if (dbManager.getPlanByTitleAndSender(mTitle, mFrom, mTime) == null) {
                sendNotification(context);
                setPlanToDB(context);
                setPlanToExecute(context);
                return;
            }
        } catch (JSONException ex) {
            return;
        }
    }

    private void sendNotification(Context pContext) {
        RemoteViews remoteViews = new RemoteViews(pContext.getPackageName(), R.layout.notification_remote_plan);
        Notification notification;
        Notification.Builder builder = new Notification.Builder(pContext);

        Intent openPlannerIntent = new Intent(pContext, PlannerActivity.class);
        openPlannerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent openPlannerPendingIntent = PendingIntent.getActivity(pContext, 20, openPlannerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setTextViewText(R.id.tv_title, mTitle);
        remoteViews.setTextViewText(R.id.tv_details, "from " + mFrom);

        builder.setSmallIcon(R.drawable.ic_alarm)
                .setContent(remoteViews)
                .setContentIntent(openPlannerPendingIntent);

        if (SystemUtils.isJellyBeanHigher()) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (sharedHelper.getVibrationOn()) {
            notification.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        }
        notificationManager.notify(101 + (int) System.currentTimeMillis(), notification);
    }

    private void setPlanToDB(Context context) {
        final Plan p = new Plan();
        p.setTitle(mTitle);
        p.setDetails(mDetails);
        p.setTimeStamp(mTime);
        p.setDaysToAlarm("0000000");
        p.setIsDeleted(0);
        p.setSender(mFrom);
        p.setIsRemote(1);
        if (mParseID != null) {
            p.setImagePath(netManager.downloadImage(mTitle, mTime, mParseID, new ImageDownload(p, context)));

        } else {
            uploadPlan(p, context);
        }
    }

    private void setPlanToExecute(Context pContext) {
        int pendingId = dbManager.getLastPlanID();
        PendingIntent pendingIntent = alarmManager.createPendingIntent(Integer.toString(pendingId));
        ((AlarmManager) pContext.getSystemService(pContext.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, mTime, pendingIntent);
    }

    final private class ImageDownload implements OnImageDownloadCompletedCallback {
        private Plan p;
        private Context context;

        public ImageDownload(Plan plan, Context context) {
            this.p = plan;
            this.context = context;
        }

        @Override
        public void downloadSuccessful() {
            uploadPlan(p, context);
        }
    }

    private void uploadPlan(final Plan p, Context context) {
        if (!sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(context)) {
            p.setIsSynchronized(0);
            dbManager.saveNewPlan(p);
            return;
        }
        p.setIsSynchronized(1);
        dbManager.saveNewPlan(p);
        netManager.addPlan(p, new OnSaveCallback() {
            @Override
            public void getId(String id, long updatedAtParseTime) {
                if (ValidData.isTextValid(id)) {
                    p.setParseId(id);
                    p.setUpdatedAtParseTime(updatedAtParseTime);
                    int planId = dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId();
                    p.setLocalId(planId);
                    dbManager.editPlan(p, planId);
                }
            }
        });
    }
}
