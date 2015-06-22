package chisw.com.plans.others;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.Receivers.NotificationReceiver;
import chisw.com.plans.core.Receivers.RebootReceiver;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.db.entity.PlansEntity;

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

    public void Reload(Context ctx) {
        DBManager dbManager = ((PApplication) ctx.getApplicationContext()).getDbManager();
        Cursor cursor = dbManager.getPlans();
        cursor.moveToFirst();
        do {
            Intent intent = new Intent(ctx, NotificationReceiver.class);
            intent.setAction(cursor.getString(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
            Toast.makeText(ctx,cursor.getString(cursor.getColumnIndex(PlansEntity.LOCAL_ID)),Toast.LENGTH_LONG);
            PendingIntent pAlarmIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP)), pAlarmIntent);
        } while (cursor.moveToNext());
    }
}
