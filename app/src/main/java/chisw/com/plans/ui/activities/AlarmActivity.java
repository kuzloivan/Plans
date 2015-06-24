package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.ui.dialogs.DatePickDialog;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.EditText;

import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.dialogs.DaysOfWeekDialog;
import chisw.com.plans.ui.dialogs.TimePickDialog;
import chisw.com.plans.utils.BitmapUtils;
import chisw.com.plans.utils.DataUtils;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;

import android.support.v4.app.DialogFragment;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AlarmActivity extends ToolbarActivity implements DaysOfWeekDialog.DaysOfWeekDialogListener {

    private final int REQUEST_AUDIO_GET = 1;
    private final int GALLERY_REQUEST = 2;
    private boolean isDialogExist;
    private int durationBuf;
    private long audioDuration;
    private boolean isAudioSelected;
    private boolean isEdit;
    private EditText etTitle;
    private EditText setDetails_textview;
    private TextView tvSoundDuration;
    private TextView tvDate;
    private TextView tvTime;
    private AlarmManager am;
    private String path;
    private DatePickDialog dateDialog;
    private DaysOfWeekDialog daysOfWeekDialog;
    private DialogFragment timeDialog;
    private Switch sRepeating;
    private ImageView iv_image;
    private Uri selectedImageURI;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackButton();
        Clicker clicker = new Clicker();
        findViewById(R.id.bt_save_alarm).setOnClickListener(clicker);
        findViewById(R.id.aa_setAudio_btn).setOnClickListener(clicker);
        findViewById(R.id.switch_repeating).setOnClickListener(clicker);
        tvDate = (TextView) findViewById(R.id.setDate_textview);
        tvTime = (TextView) findViewById(R.id.setTime_textview);
        tvSoundDuration = (TextView) findViewById(R.id.tv_sound_duration);
        tvSoundDuration.setOnClickListener(clicker);
        tvDate.setOnClickListener(clicker);
        tvTime.setOnClickListener(clicker);
        iv_image = (ImageView) findViewById(R.id.aa_image);
        iv_image.setOnClickListener(clicker);
        etTitle = (EditText) findViewById(R.id.setTitle_textview);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        setDetails_textview = (EditText) findViewById(R.id.setDetails_textview);
        sRepeating = (Switch) findViewById(R.id.switch_repeating);
        SeekerBar sb = new SeekerBar();
        final SeekBar seekbar = (SeekBar) findViewById(R.id.sb_duration_sound);
        seekbar.setOnSeekBarChangeListener(sb);

        DataUtils.initializeCalendar();
        if (getIntent().hasExtra("Plan")) {
            isEdit = getIntent().getBundleExtra("Plan").getBoolean("isEdit");
        }
        if (isEdit) {
            fillIn(seekbar);
        } else {
            tvSoundDuration.setText("0:0");
        }
        tvTime.setText(DataUtils.getTimeStrFromCalendar());
        tvDate.setText(DataUtils.getDateStrFromCalendar());
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataUtils.setCalendar(Calendar.getInstance());
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

    public static void start(Activity a, Bundle bundle) {
        Intent i = new Intent(a, AlarmActivity.class);
        i.putExtra("Plan", bundle);
        a.startActivity(i);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_alarm;
    }

    public void startAlarm() {
        if (ValidData.isTextValid(etTitle.getText().toString())) {
            if ((DataUtils.getCalendar().getTimeInMillis() - System.currentTimeMillis() > 0)) {
                writePlanToDB(DataUtils.getCalendar());
                PendingIntent pendingIntent = alarmManager.createPendingIntent(Integer.toString(dbManager.getLastPlanID()));

                if(!sRepeating.isChecked()) {
                    am.set(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), pendingIntent);
                }
                else {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                }

                finish();
            } else if (DataUtils.getCalendar().getTimeInMillis() - System.currentTimeMillis() <= 0) {
                showToast("Time is incorrect.");
            } else {
                showToast("Choose audio for notification.");
            }
        } else {
            showToast("Field is empty");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            isDialogExist = false;
            return;
        }
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                path = getPath(data);
                if (SystemUtils.isKitKatHigher()) {
                    durationBuf = getAudioDuration(data.getData(), this);
                } else {
                    Uri u = data.getData();
                    durationBuf = getAudioDuration(u, this);
                }
                isAudioSelected = true;
                isDialogExist = false;
                break;
            case GALLERY_REQUEST:
                selectedImageURI = data.getData();
                final String[] proj = {MediaStore.Images.Media.DATA};
                final Cursor cursor = getContentResolver().query(selectedImageURI, proj, null, null, null);
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                selectedImagePath = cursor.getString(column_index);

                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(selectedImagePath, 110, 110);
                iv_image.setImageBitmap(bitmap);

                break;
        }
    }

    private void chooseAudio() {
        if (isDialogExist) {
            return;
        }
        isDialogExist = true;
        Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
        chooseAudio.setType("audio/*");
        if (chooseAudio.resolveActivity(getPackageManager()) == null) {
            isDialogExist = false;
        }
        startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
       /* iv_image.setImageURI(null);
        iv_image.setImageURI(selectedImageURI);*/

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

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
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

    private void writePlanToDB(Calendar calendar) {
        final Plan p = new Plan();
        p.setDetails(setDetails_textview.getText().toString());
        p.setTitle(etTitle.getText().toString());
        p.setTimeStamp(calendar.getTimeInMillis());
        p.setAudioPath(path);
        p.setAudioDuration((int) audioDuration);
        if (isEdit) {
            p.setParseId(getIntent().getBundleExtra("Plan").getString("ParseID"));
            p.setLocalId(getIntent().getBundleExtra("Plan").getInt("LocalID"));
            dbManager.editPlan(p);
            netManager.editPlan(p, new CallbackEditPlan(p));
        } else {
            dbManager.saveNewPlan(p);
            if(!sharedHelper.getSynchronization()) {
                p.setLocalId(dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId());
                synchronization.wasAdding(p.getLocalId());
                showToast(Integer.toString(p.getLocalId()));
                return;
            }
            netManager.addPlan(p, new OnSaveCallback() {
                @Override
                public void getId(String id) {
                    p.setParseId(id);
                    p.setLocalId(dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId());
                    dbManager.editPlan(p);
                }
            });
        }
    }

    public final class SeekerBar implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Duration(seekBar);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Duration(seekBar);
        }

        private void Duration(SeekBar seekBar) {
            audioDuration = (durationBuf * seekBar.getProgress()) / 100;
            timeFormat();
        }
    }

    private void timeFormat() {
        long h = audioDuration / 3600;
        long m = (audioDuration - h * 3600) / 60;
        long s = audioDuration - (h * 3600 + m * 60);
        String durationValue;
        if (h == 0) {
            durationValue = m + ":" + s;
        } else {
            durationValue = h + ":" + m + ":" + s;
        }
        tvSoundDuration.setText(durationValue);
    }

    private void fillIn(SeekBar seekbar) {
        Bundle bundle = getIntent().getBundleExtra("Plan");
        etTitle.setText(bundle.getString("Title"));
        setDetails_textview.setText(bundle.getString("Details"));
        DataUtils.setCalendar(DataUtils.getCalendarByTimeStamp(bundle.getLong("TimeStamp")));
        path = bundle.getString("Path");
        audioDuration = bundle.getInt("Duration");
        Uri u = Uri.parse(path);
        durationBuf = getAudioDuration(u, this);
        isAudioSelected = true;
        seekbar.setProgress(getPercent((int) audioDuration, path));
        timeFormat();
    }

    private int getPercent(int val, String path) {
        Uri u = Uri.parse(path);
        return (val * 100) / getAudioDuration(u, this);
    }

    private int getAudioDuration(Uri path, Context ctx) {
        MediaMetadataRetriever mm = new MediaMetadataRetriever();
        mm.setDataSource(ctx, path);
        int durationMs = Integer.parseInt(mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        return durationMs / 1000;
    }

    public final class CallbackEditPlan implements GetCallback<ParseObject> {
        private final Plan plan;

        public CallbackEditPlan(Plan plan) {
            this.plan = plan;
        }

        @Override
        public void done(ParseObject parseObject, ParseException e) {
            if (e == null) {
                parseObject.put("name", plan.getTitle());
                parseObject.put("timeStamp", plan.getTimeStamp());
                if (ValidData.isTextValid(plan.getAudioPath())) {
                    parseObject.put("audioPath", plan.getAudioPath());
                }
                parseObject.put("details", plan.getDetails());
                parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
                parseObject.saveInBackground();
            } else {
                showToast(e.getMessage());
            }
        }
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_save_alarm:
                    startAlarm();
                    break;
                case R.id.setDate_textview:
                    dateDialog = new DatePickDialog();
                    dateDialog.show(getSupportFragmentManager(), "datePicker");
                    break;
                case R.id.setTime_textview:
                    timeDialog = new TimePickDialog();
                    timeDialog.show(getSupportFragmentManager(), "timePicker");
                    break;
                case R.id.switch_repeating:
                    if (sRepeating.isChecked()) {
                        daysOfWeekDialog = new DaysOfWeekDialog();
                        daysOfWeekDialog.show(getSupportFragmentManager(), "daysPicker");
                    }
                    break;
                case R.id.aa_setAudio_btn:
                    chooseAudio();
                    break;
                case R.id.aa_image:
                    chooseImage();
                    break;
            }
        }
    }

    @Override
    public void onDaysOfWeekPositiveClick(Bundle bundle) {

        //test. Delete later
        showToast("Sunday " + bundle.getBoolean("Sun") + "\n" +
                "Monday " + bundle.getBoolean("Mon") + "\n" +
                "Tuesday " + bundle.getBoolean("Tues") + "\n" +
                "Wednesday " + bundle.getBoolean("Wed") + "\n" +
                "Thursday " + bundle.getBoolean("Thurs") + "\n" +
                "Friday " + bundle.getBoolean("Fri") + "\n" +
                "Saturday " + bundle.getBoolean("Sat"));
    }

    @Override
    public void onDaysOfWeekNegativeClick(Bundle bundle) {

    }

}