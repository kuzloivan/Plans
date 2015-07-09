package chisw.com.dayit.core.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import chisw.com.dayit.R;

/**
 * Created by vdboo_000 on 08.07.2015.
 */
public class ParsePushNotificationReceiver extends ParseBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Notification.Builder builder = new Notification.Builder(context);
        try {
            Bundle extras = intent.getExtras();
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jObj = new JSONObject(jsonData);
            builder.setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(jObj.getString("alert"))
                    .setContentText(jObj.getString("title"));
        } catch(JSONException ex) {
            return;
        }
        Notification notification = builder.build();
        Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.sound = ringURI;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        notification.vibrate = vibrate;
        notificationManager.notify(101 + (int) System.currentTimeMillis(), notification);

        Log.i("Push", "Receive was successful");
    }

}
