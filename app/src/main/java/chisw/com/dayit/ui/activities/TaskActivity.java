package chisw.com.dayit.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;

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

/**
 * Created by Kos on 09.07.2015.
 */
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
    protected DialogFragment mTimeDialog;
    protected AlarmManager mAlarmManager;
    protected boolean mIsEdit;
    protected ImageView mIvImage;
    protected String mDaysToAlarm;
    protected int mPlanId;

    protected void initViews() {
        initBackButton();
        mTvDate = (TextView) findViewById(R.id.setDate_textview);
        mTvTime = (TextView) findViewById(R.id.setTime_textview);
        mTvDate.setText(DataUtils.getDateStrFromCalendar());
        mTvTime.setText(DataUtils.getTimeStrFromCalendar());
        mIvImage = (ImageView) findViewById(R.id.aa_image);
        mSwitchRepeating = (Switch) findViewById(R.id.switch_repeating);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackButton();
        DataUtils.initializeCalendar();

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
                startAlarm();
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
                    p.setParseId(id);
                    int planId = dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId();
                    p.setLocalId(planId);
                    dbManager.editPlan(p, planId);
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
            return;
        }
        PendingIntent pendingIntent = alarmManager.createPendingIntent(Integer.toString(pendingId));
        if (!mSwitchRepeating.isChecked()) {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), pendingIntent);
        } else {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DataUtils.getCalendar().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        finish();
    }

    protected String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if (resultCode != RESULT_OK) {
            /*mIsDialogExist = false;*/
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
        if (imageIntent.getScheme().equals("file")) {
            Uri imageUri = imageIntent.getData();
            String strImageUri = imageUri.toString();
            StringBuffer strBuf = new StringBuffer(strImageUri);
            strBuf = strBuf.delete(0, 5);
            mSelectedImagePath = strBuf.toString();
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
            mIvImage.setImageBitmap(bitmap);
        }
        if (imageIntent.getScheme().equals("content")) {
            Uri selectedImageUri = imageIntent.getData();
            mSelectedImagePath = getRealPathFromURI(selectedImageUri);
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
            mIvImage.setImageBitmap(bitmap);
        }
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

//        if (System.currentTimeMillis() > DataUtils.getCalendar().getTimeInMillis()) {
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

        DataUtils.setCalendar(DataUtils.getCalendarByTimeStamp(p.getTimeStamp()));
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
                case R.id.aa_image:
                    chooseImage();
                    break;

            }
        }
    }

    protected final class DialogDaysOfWeekClicker implements DaysOfWeekDialog.DaysOfWeekDialogListener {

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