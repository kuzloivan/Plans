package chisw.com.plans.db;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import chisw.com.plans.core.Receivers.NotificationReceiver;
import chisw.com.plans.core.bridge.AlarmBridge;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 24.06.2015.
 */
public class AlarmManager implements AlarmBridge {

    Context context;

    public AlarmManager(Context context) {
        this.context = context;
    }

    @Override
    public void cancelAlarm(Cursor cursor) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createPendingIntent(Integer.toString(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)))));
    }

    public void cancelAlarm(Plan plan) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createPendingIntent(Integer.toString(plan.getLocalId())));
    }

    @Override
    public PendingIntent createPendingIntent(String action) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
