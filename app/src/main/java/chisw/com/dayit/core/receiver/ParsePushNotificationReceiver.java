package chisw.com.dayit.core.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseBroadcastReceiver;

import chisw.com.dayit.ui.activities.PlannerActivity;

/**
 * Created by vdboo_000 on 08.07.2015.
 */
public class ParsePushNotificationReceiver extends ParseBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("Push", "Receive was successful");
    }
}
