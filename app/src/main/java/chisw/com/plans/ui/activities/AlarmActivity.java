package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import chisw.com.plans.core.callback.OnSaveCallback;
import chisw.com.plans.ui.dialogs.DatePickDialog;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.EditText;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class AlarmActivity extends ToolbarActivity{

    public static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.alarm_activity.id";
    public static final String BUNDLE_KEY = "chisw.com.plans.ui.activities.alarm_activity.bundle";

    private final int REQUEST_AUDIO_GET = 1;
    private final int GALLERY_REQUEST = 2;
    private boolean mIsDialogExist;
    private int mDurationBuf;
    private long mAudioDuration;
    private boolean mIsEdit;
    private EditText mEtTitle;
    private EditText mTvSetDetails;
    private TextView mTvSoundDuration;
    private TextView mTvDate;
    private TextView mTvTime;
    private AlarmManager mAlarmManager;
    private String mPath;
    private DatePickDialog mDatePickDialog;
    private DaysOfWeekDialog mDaysOfWeekDialog;
    private DialogFragment mTimeDialog;
    private Switch mSwitchRepeating;
    private ImageView mIvImage;
    private TextView mTextValue;
    private SeekBar mSeekBar;
    private Uri mSelectedImageURI;
    private String mSelectedImagePath;
    private String mDaysToAlarm;

    public static void start(Activity a, int id) {
        Intent i = new Intent(a, AlarmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, id);
        i.putExtra(BUNDLE_KEY, bundle);
        a.startActivity(i);
    }

    public static void start(Activity a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
    }

    private void initViews() {
        Clicker clicker = new Clicker();
        SeekBarListener sb = new SeekBarListener();
        initBackButton();
        findViewById(R.id.bt_save_alarm).setOnClickListener(clicker);
        findViewById(R.id.aa_setAudio_btn).setOnClickListener(clicker);
        mTvDate = (TextView) findViewById(R.id.setDate_textview);
        mTvDate.setOnClickListener(clicker);
        mTvTime = (TextView) findViewById(R.id.setTime_textview);
        mTvTime.setOnClickListener(clicker);
        mTvDate.setText(DataUtils.getDateStrFromCalendar());
        mTvTime.setText(DataUtils.getTimeStrFromCalendar());
        mTvSoundDuration = (TextView) findViewById(R.id.tv_sound_duration);
        mTvSoundDuration.setOnClickListener(clicker);
        mTextValue = (TextView) findViewById(R.id.alarmSoundTitle_textview);
        mIvImage = (ImageView) findViewById(R.id.aa_image);
        mIvImage.setOnClickListener(clicker);
        mSwitchRepeating = (Switch) findViewById(R.id.switch_repeating);
        mSwitchRepeating.setOnClickListener(clicker);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mSeekBar = (SeekBar) findViewById(R.id.sb_duration_sound);
        mSeekBar.setOnSeekBarChangeListener(sb);
        mSeekBar.setEnabled(false);
        mTvSetDetails = (EditText) findViewById(R.id.setDetails_textview);
        mDaysToAlarm = "0000000";
        mTvSetDetails.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (((EditText) v).getLineCount() >= 5)
                        return true;
                }
                return false;
            }
        });
        mEtTitle = (EditText) findViewById(R.id.setTitle_textview);
        mEtTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (((EditText) v).getLineCount() >= 2)
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        DataUtils.initializeCalendar();
        if (getIntent().hasExtra(BUNDLE_KEY)) {
            mIsEdit = true;
        }
        if (mIsEdit) {
            fillIn(mSeekBar);
            int id = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
            String daysToAlarm = dbManager.getDaysToAlarmById(id);
            if(daysToAlarm.charAt(0) == '1') {
                mSwitchRepeating.setChecked(true);
                mDaysToAlarm = daysToAlarm.substring(1, daysToAlarm.length() - 1);
            }
        } else {
            mTvSoundDuration.setText("00:00");
        }
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

    @Override
    protected int contentViewResId() {
        return R.layout.activity_alarm;
    }

    private void writePlanToDB(Calendar calendar) {
        final Plan p = new Plan();
        p.setDetails(mTvSetDetails.getText().toString());
        p.setTitle(mEtTitle.getText().toString());
        p.setTimeStamp(calendar.getTimeInMillis());
        p.setAudioPath(mPath);
        p.setImagePath(mSelectedImagePath);
        p.setAudioDuration((int) mAudioDuration);
        p.setDaysToAlarm((mSwitchRepeating.isChecked() ? "1" : "0") + mDaysToAlarm);  //DOW
        p.setIsDeleted(0);

        if (mIsEdit) {
            int id = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
            p.setParseId(dbManager.getPlanById(id).getParseId());
            p.setLocalId(dbManager.getPlanById(id).getLocalId());
            if (!sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(getApplicationContext())) {
                p.setIsSynchronized(0);
                dbManager.editPlan(p, id);
                return;
            }
            p.setIsSynchronized(1);
            dbManager.editPlan(p, id);
            netManager.editPlan(p, new CallbackEditPlan(p));
        } else {
            if (!sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(getApplicationContext())) {
                p.setIsSynchronized(0);
                dbManager.saveNewPlan(p);
                return;
            }
            p.setIsSynchronized(1);
            dbManager.saveNewPlan(p);
            netManager.addPlan(p, new OnSaveCallback() {
                @Override
                public void getId(String id) {
                    p.setParseId(id);
                    int planId = dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId();
                    p.setLocalId(planId);
                    dbManager.editPlan(p, planId);
                }
            });
        }
    }

    public void startAlarm() {
        if (!ValidData.isTextValid(mEtTitle.getText().toString())) {
            showToast("Title is empty");
            return;
        }
        if (DataUtils.getCalendar().getTimeInMillis() - System.currentTimeMillis() <= 0) {
            showToast("Time is incorrect.");
            return;
        }
        writePlanToDB(DataUtils.getCalendar());
        int pendingId = dbManager.getLastPlanID();
        if (mIsEdit)
            pendingId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        PendingIntent pendingIntent = alarmManager.createPendingIntent(Integer.toString(pendingId));
        if (!mSwitchRepeating.isChecked()) {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), pendingIntent);
        } else {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            mIsDialogExist = false;
            return;
        }
        mSeekBar.setEnabled(true);
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                mPath = getPath(data);
                mIsDialogExist = false;
                String buf;
                Uri u = data.getData();
                buf = getName(u, mPath);
                if (!isValidFormat(buf)) {
                    mTvSoundDuration.setText("00:00");
                    mTextValue.setText("");
                    showToast("File is not valid");
                    return;
                }
                if (SystemUtils.isKitKatHigher()) {
                    mDurationBuf = getAudioDuration(data.getData(), this);
                } else {
                    mDurationBuf = getAudioDuration(u, this);
                }
                mTextValue.setText(buf);
                duration(mSeekBar);
                break;

            case GALLERY_REQUEST:
                /*final String[] proj = {MediaStore.Images.Media.DATA};
                final Cursor cursor;
                mSelectedImageURI = data.getData();
                cursor = getContentResolver().query(mSelectedImageURI, proj, null, null, null);
                final int column_index_i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                mSelectedImagePath = cursor.getString(column_index_i);*/

                mSelectedImagePath = getPath(data);

                //Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, 110, 110);

                int targetW = mIvImage.getWidth();
                int targetH = mIvImage.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mSelectedImagePath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                //Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath, bmOptions);
                Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
                mIvImage.setImageBitmap(bitmap);

                //mIvImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_add_white_18dp));
                break;
        }
    }

    private boolean isValidFormat(String path) {
        Pattern p = Pattern.compile(".*\\.mp3$");
        Matcher m = p.matcher(path);
        return m.matches();
    }

    private String getName(Uri pathUri, String pathName) {
        String[] arrPath = null;
        if (!SystemUtils.isKitKatHigher()) {
            final String[] proj = {MediaStore.Audio.Media.DATA};
            final Cursor cursor;
            cursor = getContentResolver().query(pathUri, proj, null, null, null);
            final int column_index_a = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToLast();
            pathName = cursor.getString(column_index_a);
            cursor.close();
        }
        arrPath = pathName.split("/");
        return arrPath[arrPath.length - 1];
    }

    private String getPath(Intent str) {
        Uri data = str.getData();
        if(data.getScheme().equals("file")){
            return data.getPath();
        }
        if (!SystemUtils.isKitKatHigher() || !DocumentsContract.isDocumentUri(this, data)) {
            return str.getDataString();
        }
        final String docId = DocumentsContract.getDocumentId(data);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri = null;
        if ("com.android.providers.media.documents".equals(data.getAuthority())) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };
        return getDataColumn(this, contentUri, selection, selectionArgs);
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

    private void chooseAudio() {
        if (mIsDialogExist) {
            return;
        }
        mIsDialogExist = true;
        Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
        chooseAudio.setType("audio/*");
        if (chooseAudio.resolveActivity(getPackageManager()) == null) {
            mIsDialogExist = false;
        }
        startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
    }

    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void duration(SeekBar seekBar) {
        mAudioDuration = (mDurationBuf * seekBar.getProgress()) / 100;
        timeFormat();
    }

    private void timeFormat() {
        mTvSoundDuration.setText(DataUtils.getTimeStrFromTimeStamp((int) mAudioDuration));
    }

    private void fillIn(SeekBar seekbar) {
        int id = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        Plan p = dbManager.getPlanById(id);
        mEtTitle.setText(p.getTitle());
        seekbar.setEnabled(true);
        mTvSetDetails.setText(p.getDetails());

        mTvDate.setText(DataUtils.getDateStringFromTimeStamp(p.getTimeStamp()));
        mTvTime.setText(DataUtils.getTimeStringFromTimeStamp(p.getTimeStamp()));

        mSelectedImagePath = p.getImagePath();
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, 110, 110);
        mIvImage.setImageBitmap(bitmap);
        if(mSelectedImagePath == null){
            mIvImage.setImageResource(R.drawable.aa_icon);
        }

        DataUtils.setCalendar(DataUtils.getCalendarByTimeStamp(p.getTimeStamp()));
        mPath = p.getAudioPath();
        if (mPath == null) {
            return;
        }
        Uri u = Uri.parse(mPath);
        mAudioDuration = p.getAudioDuration();
        mDurationBuf = getAudioDuration(u, this);
        Uri tmpUri = Uri.parse(mPath);
        mTextValue.setText(getName(tmpUri, mPath));
        seekbar.setProgress(getPercent((int) mAudioDuration, mPath));
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

    public final class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            duration(mSeekBar);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            duration(mSeekBar);
        }
    }

    public final class CallbackEditPlan implements GetCallback<ParseObject> {
        private final Plan plan;
        public CallbackEditPlan(Plan plan) {
            this.plan = plan;
        }

        @Override
        public void done(ParseObject parseObject, ParseException e) {
            if (e == null) {
                parseObject.put("title", plan.getTitle());
                parseObject.put("timeStamp", plan.getTimeStamp());
                if (ValidData.isTextValid(plan.getAudioPath())) {
                    parseObject.put("audioPath", plan.getAudioPath());
                }
                if (ValidData.isTextValid(plan.getImagePath())){
                    parseObject.put("imagePath", plan.getImagePath());
                }
                parseObject.put("audioDuration", plan.getAudioDuration());
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
                    mDatePickDialog = new DatePickDialog();
                    mDatePickDialog.show(getSupportFragmentManager(), "datePicker");
                    break;
                case R.id.setTime_textview:
                    mTimeDialog = new TimePickDialog();
                    mTimeDialog.show(getSupportFragmentManager(), "timePicker");
                    break;
                case R.id.switch_repeating:
                    if (mSwitchRepeating.isChecked()) {
                        mDaysOfWeekDialog = new DaysOfWeekDialog();

                        Bundle days = new Bundle();
                        days.putString("mDaysToAlarm", mDaysToAlarm);
                        mDaysOfWeekDialog.setArguments(days);

                        mDaysOfWeekDialog.show(getSupportFragmentManager(), "daysOfWeekPicker");
                        mDaysOfWeekDialog.setListener(new DialogDaysOfWeekClicker());
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

    private final class DialogDaysOfWeekClicker implements  DaysOfWeekDialog.DaysOfWeekDialogListener{

        @Override
        public void onDaysOfWeekPositiveClick(String pDaysOfWeek) {
            mDaysToAlarm = pDaysOfWeek; //DOW
        }

        @Override
        public void onDaysOfWeekNegativeClick(String pString) {
            mSwitchRepeating.setChecked(false);
        }
    }
}