package chisw.com.dayit.db;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import chisw.com.dayit.core.bridge.AlarmBridge;
import chisw.com.dayit.core.receiver.NotificationReceiver;


public class AlarmManager implements AlarmBridge {

    Context context;

    public AlarmManager(Context context) {
        this.context = context;
    }

    @Override
    public void cancelAlarm(int id) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createPendingIntent(Integer.toString(id)));
    }

    @Override
    public PendingIntent createPendingIntent(String action) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
