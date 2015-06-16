package chisw.com.plans.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chisw.com.plans.R;
import chisw.com.plans.core.Receiver;

/**
 * Created by Yuriy on 15.06.2015.
 */

public class AlarmActivity extends Activity{

    final String LOG_TAG = "myLogs";

    NotificationManager nm;
    AlarmManager am;
    Intent intent1;
    Intent intent2;
    PendingIntent pIntent1;
    PendingIntent pIntent2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Clicker c = new Clicker();
        findViewById(R.id.bt_ret).setOnClickListener(c);
        findViewById(R.id.bt_notif).setOnClickListener(c);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);


    }

    private Intent createIntent(String action, String extra) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }



    void sendNotif(int id, PendingIntent pIntent) {
        Notification notif = new Notification(R.mipmap.ic_launcher, "Notif "
                + id, System.currentTimeMillis());
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(this, "Title " + id, "Content " + id, pIntent);
        nm.notify(id, notif);
    }

    public void showNotification() {
        intent1 = createIntent("action 1", "extra 1");
        pIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);

        intent2 = createIntent("action 2", "extra 2");
        pIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);


        sendNotif(1, pIntent1);
        sendNotif(2, pIntent2);
    }

    public static void start(Activity a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
        //activity.startActivity(new Intent(activity, AlarmActivity.class));
    }

    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.bt_ret:
                    finish();
                    break;
                case R.id.bt_notif:
                    showNotification();
                    break;
            }
        }
    }
}
