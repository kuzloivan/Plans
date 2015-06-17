package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.Dialog;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.core.Receiver;

/**
 * Created by Yuriy on 15.06.2015.
 */

public class AlarmActivity extends ToolbarActivity{

    private static final String LOG = AlarmActivity.class.getSimpleName();

    private static final int DIALOG_TIME = 1; //id диалога //

    //NotificationManager nm;

    AlarmManager am;

    PendingIntent pAlarmIntent;
    int myHour = 0;
    int myMinute = 0;
    TextView tvTime;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Alarm");//todo set title in manifest
        initBackButton();

        setContentView(R.layout.activity_alarm);
        Clicker c = new Clicker();
        //Clicker d = new Clicker();
        findViewById(R.id.bt_notif).setOnClickListener(c);
        findViewById(R.id.bt_cancel_alarm).setOnClickListener(c);
        findViewById(R.id.bt_add_time).setOnClickListener(c);
        //nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        tvTime = (TextView) findViewById(R.id.tv_alarm_time);
        tvInfo = (TextView) findViewById(R.id.tv_alarm_info);

    }

    public static void start(Activity a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_alarm;
    }



    private Intent createIntent(String action, String extra) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }

    public void showNotification() {
        Intent intent = createIntent("action 1", "extra 1");
        pAlarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, myHour);
        calendar.set(Calendar.MINUTE, myMinute);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pAlarmIntent);

        //am.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, pAlarmIntent);

        //am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, 5000, pAlarmIntent);
    }

    public void setAlarmTime(){
        showDialog(DIALOG_TIME);
    }  // todo remove deprecated method

    protected Dialog onCreateDialog(int id) {// todo remove deprecated method
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, new TimerCallbacks(), myHour, myMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }


    public void cancelAlarm(){
        am.cancel(pAlarmIntent);
    }

    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.bt_notif:
                    showNotification();
                    break;
                case R.id.bt_cancel_alarm:
                    cancelAlarm();
                    break;
                case R.id.bt_add_time:
                    setAlarmTime();
                    break;
            }
        }
    }

    private class TimerCallbacks implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            tvTime.setText("Alarm in  " + myHour + " : " + myMinute + " ");
        }
    }
}