package chisw.com.dayit.others;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.core.receiver.NotificationReceiver;
import chisw.com.dayit.db.DBManager;
import chisw.com.dayit.db.entity.PlansEntity;

/**
 * Created by Kos on 22.06.2015.
 */
public class RestartManager {
    private AlarmManager alarmManager;
    private Context ctx;

    public RestartManager(Context ctx) {
        alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        this.ctx = ctx;
    }

    public void reload() {
        DBManager dbManager = ((PApplication) ctx.getApplicationContext()).getDbManager();
        Cursor cursor = dbManager.getPlans();
        cursor.moveToFirst();
        do {
            long time = cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP));
            if(System.currentTimeMillis() > time){
                cursor.moveToNext();
                continue;
            }
            Intent intent = new Intent(ctx, NotificationReceiver.class);
            intent.setAction(cursor.getString(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
            PendingIntent pAlarmIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP)), pAlarmIntent);
        } while (cursor.moveToNext());
        cursor.close();
    }
}
