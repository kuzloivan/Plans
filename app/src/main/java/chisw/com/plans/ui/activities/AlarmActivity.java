package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Log;
import android.view.View;

import java.util.Date;

import chisw.com.plans.R;
import chisw.com.plans.core.Receiver;

/**
 * Created by Yuriy on 15.06.2015.
 */

public class AlarmActivity extends ToolbarActivity{

    final String LOG_TAG = "myLogs";

    NotificationManager nm;
    AlarmManager am;
    Intent intent1;
    PendingIntent pIntent1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Alarm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_alarm);
        Clicker c = new Clicker();
        findViewById(R.id.bt_notif).setOnClickListener(c);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    private Intent createIntent(String action, String extra) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }



    @Override
    protected int contentViewResId() {
        return R.layout.activity_alarm;
    }


    public void showNotification() {
        intent1 = createIntent("action 1", "extra 1");
        pIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        Log.d(LOG_TAG, "start");



        Log.d(LOG_TAG, "start");
        am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pPendIntent1);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendNotif(1, pPendIntent1);
//            }
//        }, 2000);

        am.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 3000, 5000, pPendIntent1);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, pIntent1);
    }

    public static void start(Context a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
        //activity.startActivity(new Intent(activity, AlarmActivity.class));
    }




    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.bt_notif:
                    //showNotification();
                    break;
            }
        }
    }
}
