package chisw.com.plans.core.bridge;

import android.app.PendingIntent;
import android.database.Cursor;

/**
 * Created by Alexander on 24.06.2015.
 */
public interface AlarmBridge {
    void cancelAlarm(Cursor cursor);
    PendingIntent createPendingIntent(String action);
}
