package chisw.com.plans.core.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.PlannerActivity;

/**
 * Created by Yuriy on 25.06.2015.
 */
public class NotificationCancelReceiver extends BroadcastReceiver {
    public void onReceive(Context ctx, Intent intent)
    {
        String action = intent.getAction();
        if(action.equals("notification_cancelled")) {
            Multimedia multimedia = (((PApplication) ctx.getApplicationContext()).getMultimedia());
            multimedia.stopPlayer();
        }
    }
}
