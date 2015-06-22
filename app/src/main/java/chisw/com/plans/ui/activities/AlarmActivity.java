package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import chisw.com.plans.others.DatePicker;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

import chisw.com.plans.R;
import chisw.com.plans.core.Receivers.Receiver;
import chisw.com.plans.model.Plan;
import chisw.com.plans.others.Multimedia;
import chisw.com.plans.others.TimePickFragment;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Yuriy on 15.06.2015.
 */

public class AlarmActivity extends ToolbarActivity {

    private static final int REQUEST_AUDIO_GET = 1;
    private String path;
    private boolean isChAudioExist;
    public static boolean isAudioSelected;
    private static Calendar calendar = Calendar.getInstance();
    private TextView mTextValue;

    AlarmManager am;
    PendingIntent pAlarmIntent;
    EditText et;
    chisw.com.plans.others.DatePicker dateDialog;
    DialogFragment timeDialog;
    TextView tvDate;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackButton();

        Clicker c = new Clicker();
        findViewById(R.id.bt_save_alarm).setOnClickListener(c);
        findViewById(R.id.aa_setAudio_btn).setOnClickListener(c);
        findViewById(R.id.bt_save_alarm_date).setOnClickListener(c);
        findViewById(R.id.bt_save_alarm_time).setOnClickListener(c);
//        ======= Please, don't delete ======
//        tp = (TimePicker) findViewById(R.id.timePicker);
//        tp.setIs24HourView(android.text.format.DateFormat.is24HourFormat(this));
//        Calendar cal = Calendar.getInstance();
//        tp.setCurrentHour(cal.get(Calendar.AM_PM));
//        if(cal.get(Calendar.AM_PM)==0){
//            tp.setCurrentHour(cal.get(Calendar.HOUR));
//        }
//        else{
//            tp.setCurrentHour(cal.get(Calendar.HOUR)+12);
//        }
//        tp.setCurrentMinute(cal.get(Calendar.MINUTE)+1);
//        dp = (chisw.com.plans.others.DatePicker) findViewById(R.id.datePicker);
//        dp.setMinDate(System.currentTimeMillis() - 1000);
        et = (EditText) findViewById(R.id.setTitle_textview);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);

        int monthTmp = calendar.get(Calendar.MONTH) + 1;
        tvDate.setText("Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "-" + monthTmp + "-" + calendar.get(Calendar.YEAR));
        tvTime.setText("Time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        AlarmActivity.setCalendarSeconds(0);

        //======Play with seekBar======
        SeekerBar sb = new SeekerBar();
        final SeekBar seekbar = (SeekBar)findViewById(R.id.sb_duration_sound);
        seekbar.setOnSeekBarChangeListener(sb);
        mTextValue = (TextView)findViewById(R.id.tv_show_duration_sound);
        mTextValue.setText("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aa_save_alarm:
                startAlarm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Activity a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_alarm;
    }

    private Intent createIntent(String action) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(action);
        return intent;
    }

    public void startAlarm() {

        if (ValidData.isTextValid(et.getText().toString())) {
            if (/*isAudioSelected && */(calendar.getTimeInMillis() - System.currentTimeMillis() > 0)) {
                writeToDB(calendar);
                Intent intent = createIntent(Integer.toString(dbManager.getLastPlanID()));
                pAlarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pAlarmIntent);
                finish();
            }
            else if (calendar.getTimeInMillis() - System.currentTimeMillis() <= 0) {
                showToast("Time is incorrect.");
            }
            else {
                showToast("Choose audio for notification.");
            }
        } else {
            showToast("Field is empty");
        }
    }

    public void cancelAlarm() {
        am.cancel(pAlarmIntent);
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_save_alarm:
                    startAlarm();
                    break;
                case R.id.bt_save_alarm_date:
                    dateDialog = new chisw.com.plans.others.DatePicker();
                    dateDialog.show(getSupportFragmentManager(), "datePicker");
                    break;
                case R.id.bt_save_alarm_time:
                    timeDialog = new TimePickFragment();
                    timeDialog.show(getSupportFragmentManager(), "timePicker");
                    break;
                case R.id.aa_setAudio_btn:
                    chooseAudio();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            isChAudioExist = false;
            return;
        }
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                path = getPath(data);
                sharedHelper.setDefaultMediaWay(path);
                isAudioSelected = true;
                isChAudioExist = false;
                break;
        }
    }

    private void chooseAudio() {
        if (isChAudioExist) {
            return;
        }
        isChAudioExist = true;
        Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
        chooseAudio.setType("audio/*");
        if (chooseAudio.resolveActivity(getPackageManager()) == null) {
            isChAudioExist = false;
        }
        startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
    }

    private String getPath(Intent str) {
        if (SystemUtils.isKitKatHigher()) {

            Uri data = str.getData();
            final String docId = DocumentsContract.getDocumentId(data);
            final String[] split = docId.split(":");
            Uri contentUri = null;
            if ("com.android.providers.media.documents".equals(data.getAuthority())) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    split[1]
            };

            return getDataColumn(this, contentUri, selection, selectionArgs);
        } else {
            return str.getDataString();
        }
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void writeToDB(Calendar calendar) {
        Plan p = new Plan();
        p.setTitle(et.getText().toString());
        p.setTimeStamp(calendar.getTimeInMillis());
        p.setAudioPath(path);
        dbManager.saveNewPlan(p);
    }

    public static void setCalendarYear(int year){
        AlarmActivity.calendar.set(Calendar.YEAR, year);
    }

    public static void setCalendarMonth(int month){
        AlarmActivity.calendar.set(Calendar.MONTH, month);
    }

    public static void setCalendarDay(int day){
        AlarmActivity.calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public static void setCalendarHour(int hour){
        AlarmActivity.calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    public static void setCalendarMinute(int minute){
        AlarmActivity.calendar.set(Calendar.MINUTE, minute);
    }

    public static void setCalendarSeconds(int seconds){
        AlarmActivity.calendar.set(Calendar.SECOND, 0);
    }
    public final class SeekerBar implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
            Multimedia.PLAYING_AUDIO_TIME = seekBar.getProgress();
            mTextValue.setText(String.valueOf(seekBar.getProgress()));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Multimedia.PLAYING_AUDIO_TIME = seekBar.getProgress();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Multimedia.PLAYING_AUDIO_TIME = seekBar.getProgress();
            showToast("PLAYING_AUDIO_TIME = "+ seekBar.getProgress());
            mTextValue.setText(String.valueOf(seekBar.getProgress()));
        }

    }
}