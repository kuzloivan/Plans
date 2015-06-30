package chisw.com.plans.core.bridge;

import android.app.PendingIntent;

public interface AlarmBridge {
    void cancelAlarm(int id);
    PendingIntent createPendingIntent(String action);
}
