package chisw.com.plans.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.others.Multimedia;

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
