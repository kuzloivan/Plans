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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.core.Receivers.Receiver;
import chisw.com.plans.model.Plan;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;


/**
 * Created by Yuriy on 15.06.2015.
 */

public class AlarmActivity extends ToolbarActivity {

    private static final String LOG = AlarmActivity.class.getSimpleName();

    private static final int REQUEST_AUDIO_GET = 1;
    private String path;
    private boolean isChAudioExist;
    private boolean isAudioSelected;

    AlarmManager am;
    PendingIntent pAlarmIntent;

    TimePicker tp;
    DatePicker dp;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackButton();

        Clicker c = new Clicker();
        findViewById(R.id.bt_save_alarm).setOnClickListener(c);
        findViewById(R.id.bt_cancel_alarm).setOnClickListener(c);
        findViewById(R.id.aa_setAudio_btn).setOnClickListener(c);

        tp = (TimePicker) findViewById(R.id.timePicker);
        tp.setIs24HourView(android.text.format.DateFormat.is24HourFormat(this));
        dp = (DatePicker) findViewById(R.id.datePicker);
        dp.setMinDate(System.currentTimeMillis() - 1000);
        et = (EditText) findViewById(R.id.setTitle_textview);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
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

    //========================ALARM=================================

    private Intent createIntent(String action, String extra) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(action);
        intent.putExtra("extra", extra);
        return intent;
    }

    public void startAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
        calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
        calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
        calendar.set(Calendar.MONTH, dp.getMonth());
        calendar.set(Calendar.YEAR, dp.getYear());

        if (ValidData.isTextValid(et.getText().toString())) {
            if (isAudioSelected) {
                writeToDB(calendar);
                Intent intent = createIntent(Integer.toString(dbManager.getLastPlanID()), "extra");
                pAlarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pAlarmIntent);
                //writeToDB(calendar);
                finish();
            } else {
                showToast("Choose audio for notification.");
            }
        } else {
            showToast("Field is empty");
        }
        //PlannerActivity.start(this);
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
                case R.id.bt_cancel_alarm:
                    cancelAlarm();
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
        dbManager.saveNewPlan(p);
    }
}