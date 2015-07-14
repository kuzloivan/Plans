package chisw.com.dayit.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chisw.com.dayit.R;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.ui.dialogs.DatePickDialog;
import chisw.com.dayit.ui.dialogs.DaysOfWeekDialog;
import chisw.com.dayit.ui.dialogs.TimePickDialog;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public abstract class TaskActivity extends ToolbarActivity {

    protected final int GALLERY_REQUEST = 2;

    protected boolean mIsDialogExist;
    protected EditText mEtTitle;
    protected EditText mTvSetDetails;
    protected TextView mTvSoundDuration;
    protected TextView mTvDate;
    protected TextView mTvTime;
    protected String mSelectedImagePath;
    protected Switch mSwitchRepeating;
    protected DatePickDialog mDatePickDialog;
    protected DaysOfWeekDialog mDaysOfWeekDialog;
    protected TimePickDialog mTimeDialog;
    protected AlarmManager mAlarmManager;
    protected boolean mIsEdit;
    protected ImageView mIvImage;
    protected String mDaysToAlarm;
    protected int mPlanId;
    protected Calendar mMyLovelyCalendar;

    protected void initViews() {
        initBackButton();
        mMyLovelyCalendar = Calendar.getInstance();
        mMyLovelyCalendar.set(Calendar.SECOND, 0);
        mTvDate = (TextView) findViewById(R.id.ta_setDate_textView);
        mTvTime = (TextView) findViewById(R.id.ta_setTime_textView);
        mTvDate.setText(DataUtils.getDateStrFromCalendar(mMyLovelyCalendar));
        mTvTime.setText(DataUtils.getTimeStrFromCalendar(mMyLovelyCalendar));
        mIvImage = (ImageView) findViewById(R.id.ta_planImage_imageView);
        mSwitchRepeating = (Switch) findViewById(R.id.ta_repeatingTrig_switch);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mTvSetDetails = (EditText) findViewById(R.id.ta_details_textView);
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
        mEtTitle = (EditText) findViewById(R.id.ta_title_textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackButton();

    }

    protected void dateFillIn() {
        if (mIsEdit) {
            String daysToAlarm = dbManager.getDaysToAlarmById(mPlanId);
            if (daysToAlarm != null) {
                if (daysToAlarm.charAt(0) == '1') {
                    mSwitchRepeating.setChecked(true);
                    mDaysToAlarm = daysToAlarm.substring(1, daysToAlarm.length() - 1);
                }
                mSwitchRepeating.setChecked(daysToAlarm.charAt(0) == '1');
                mDaysToAlarm = daysToAlarm.substring(1, daysToAlarm.length() - 1);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                if(!checkWithRegExp(mEtTitle.getText().toString(), getString(R.string.title_pttrn)))
                    startAlarm();
                else
                    Toast.makeText(this, "No title has been detected", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_task;
    }

    protected void writePlanToDB(Calendar calendar, Plan plan) {

        final Plan p = plan;
        p.setDetails(mTvSetDetails.getText().toString());
        p.setTitle(mEtTitle.getText().toString());
        p.setTimeStamp(calendar.getTimeInMillis());
        p.setImagePath(mSelectedImagePath);
        p.setDaysToAlarm((mSwitchRepeating.isChecked() ? "1" : "0") + mDaysToAlarm);  //DOW
        p.setIsDeleted(0);

        if (mIsEdit) {

            p.setParseId(dbManager.getPlanById(mPlanId).getParseId());
            p.setLocalId(dbManager.getPlanById(mPlanId).getLocalId());
            if (!sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(getApplicationContext())) {
                p.setIsSynchronized(0);
                dbManager.editPlan(p, mPlanId);
                return;
            }
            p.setIsSynchronized(1);
            dbManager.editPlan(p, mPlanId);
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
                    if (ValidData.isTextValid(id)) {
                        p.setParseId(id);
                        int planId = dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId();
                        p.setLocalId(planId);
                        dbManager.editPlan(p, planId);
                    }
                }
            });
        }
    }

    protected void startAlarm() {
        int pendingId = dbManager.getLastPlanID();
        if (mIsEdit) {
            pendingId = mPlanId;
        }
        if (dbManager.getPlanById(pendingId).getIsRemote() == 1) {
            finish();
            return;
        }
        PendingIntent pendingIntent = alarmManager.createPendingIntent(Integer.toString(pendingId));
        if (!mSwitchRepeating.isChecked()) {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mMyLovelyCalendar.getTimeInMillis(), pendingIntent);
        } else {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mMyLovelyCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case GALLERY_REQUEST:
                setImageFromGallery(returnedIntent);
                break;
        }
    }

    protected void setImageFromGallery(Intent imageIntent) {
        int targetW = mIvImage.getWidth();
        int targetH = mIvImage.getHeight();
        mSelectedImagePath = getPath(imageIntent.getData());
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
        mIvImage.setImageBitmap(bitmap);
    }

    protected String getName(Uri pathUri, String pathName) {
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

    protected String getPath(Uri data) {

        if (SystemUtils.isKitKatHigher() && DocumentsContract.isDocumentUri(this, data)) {
            // ExternalStorageProvider

            if (isExternalStorageDocument(data)) {
                final String docId = DocumentsContract.getDocumentId(data);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(data)) {

                final String id = DocumentsContract.getDocumentId(data);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(data)) {
                final String docId = DocumentsContract.getDocumentId(data);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(data.getScheme())) {
            return getDataColumn(this, data, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(data.getScheme())) {
            return data.getPath();
        }

        return null;
    }

    protected String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

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

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    protected void chooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    protected boolean checkFields() {
        if (!ValidData.isTextValid(mEtTitle.getText().toString())) {
            showToast("Title is empty");
            return false;
        }

        //todo Make time verification
//        if (System.currentTimeMillis() > DataUtils.getCalendarByTimeStamp(p)) {
//            showToast("Time is incorrect.");
//            return false;
//        }

        return true;
    }

    protected void fillIn(Plan p) {
        mEtTitle.setText(p.getTitle());
        mTvSetDetails.setText(p.getDetails());
        mTvDate.setText(DataUtils.getDateStringFromTimeStamp(p.getTimeStamp()));
        mTvTime.setText(DataUtils.getTimeStringFromTimeStamp(p.getTimeStamp()));

        mSelectedImagePath = p.getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
        mIvImage.setImageBitmap(bitmap);
        if (mSelectedImagePath == null) {
            mIvImage.setImageResource(R.drawable.aa_icon);
        }

        mMyLovelyCalendar = DataUtils.getCalendarByTimeStamp(p.getTimeStamp());
    }

    protected final class CallbackEditPlan implements GetCallback<ParseObject> {
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
                if (ValidData.isTextValid(plan.getImagePath())) {
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

    protected class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ta_setDate_textView:
                    mDatePickDialog = new DatePickDialog();
                    mDatePickDialog.show(getSupportFragmentManager(), "datePicker");
                    mDatePickDialog.setListener(new DialogClicker());
                    break;
                case R.id.ta_setTime_textView:
                    mTimeDialog = new TimePickDialog();
                    mTimeDialog.show(getSupportFragmentManager(), "timePicker");
                    mTimeDialog.setListener(new DialogClicker());
                    break;
                case R.id.ta_repeatingTrig_switch:
                    if (mSwitchRepeating.isChecked()) {
                        mDaysOfWeekDialog = new DaysOfWeekDialog();

                        Bundle days = new Bundle();
                        days.putString("mDaysToAlarm", mDaysToAlarm);
                        mDaysOfWeekDialog.setArguments(days);

                        mDaysOfWeekDialog.show(getSupportFragmentManager(), "daysOfWeekPicker");
                        mDaysOfWeekDialog.setListener(new DialogClicker());
                    }
                    break;
                case R.id.ta_planImage_imageView:
                    chooseImage();
                    break;

            }
        }
    }

    protected final class DialogClicker implements DaysOfWeekDialog.DaysOfWeekDialogListener,
            DatePickDialog.DatePickListener, TimePickDialog.TimePickListener {

        @Override
        public void onDaysOfWeekPositiveClick(String pDaysOfWeek) {
            mDaysToAlarm = pDaysOfWeek; //DOW
        }

        @Override
        public void onDaysOfWeekNegativeClick(String pString) {
            mSwitchRepeating.setChecked(false);
        }

        @Override
        public void onDatePickPositiveClick(int pYear, int pMonth, int pDay) {

            mMyLovelyCalendar.set(Calendar.YEAR, pYear);
            mMyLovelyCalendar.set(Calendar.MONTH, pMonth);
            mMyLovelyCalendar.set(Calendar.DAY_OF_MONTH, pDay);

            mTvDate.setText(DataUtils.getDateStrFromCalendar(mMyLovelyCalendar));
        }

        @Override
        public void onTimePickPositiveClick(int pHour, int pMinute) {

            mMyLovelyCalendar.set(Calendar.HOUR_OF_DAY, pHour);
            mMyLovelyCalendar.set(Calendar.MINUTE, pMinute);

            mTvTime.setText(DataUtils.getTimeStrFromCalendar(mMyLovelyCalendar));
        }
    }

    public static boolean checkWithRegExp(String userNameString, String regularExpr){
        Pattern p = Pattern.compile(regularExpr);
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }
}
