package chisw.com.dayit.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chisw.com.dayit.R;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.ui.dialogs.ChooseImageDialog;
import chisw.com.dayit.ui.dialogs.DatePickDialog;
import chisw.com.dayit.ui.dialogs.DaysOfWeekDialog;
import chisw.com.dayit.ui.dialogs.TimePickDialog;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public abstract class TaskActivity extends ToolbarActivity {

    protected final int GALLERY_REQUEST = 2;
    protected final int REQUEST_TAKE_PHOTO = 5;

    protected boolean mIsDialogExist;
    protected EditText mEtTitle;
    protected EditText mTvSetDetails;
    protected TextView mTvSoundDuration;
    protected TextView mTvDate;
    protected TextView mTvTime;
    protected String mSelectedImagePath;
    protected Switch mSwitchRepeating;
    protected DatePickDialog mDatePickDialog;
    protected TimePickDialog mTimeDialog;
    protected AlarmManager mAlarmManager;
    protected boolean mIsEdit;
    protected ImageView mIvImage;
    protected String mDaysToAlarm;
    protected int mPlanId;
    protected long mUpdatedAtParseTime;
    protected Calendar mMyLovelyCalendar;
    protected ArrayList<CheckBox> mCheckBoxesArr;


    protected void initViews() {
        initBackButton();

        mMyLovelyCalendar = Calendar.getInstance(); // edit bug (calendar displays incorrect)

        mMyLovelyCalendar.set(Calendar.SECOND, 0);
        mTvDate = (TextView) findViewById(R.id.ta_setDate_textView);
        mTvTime = (TextView) findViewById(R.id.ta_setTime_textView);
        mTvDate.setText(DataUtils.getDateStrFromCalendar(mMyLovelyCalendar));
        mTvTime.setText(DataUtils.getTimeStrFromCalendar(mMyLovelyCalendar));
        mIvImage = (ImageView) findViewById(R.id.ta_planImage_imageView);
        mSwitchRepeating = (Switch) findViewById(R.id.ta_repeatingTrig_switch);
        mSwitchRepeating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mSwitchRepeating.isChecked()) {
                    setEnabledCheckBoxes(false);
                    mTvDate.setVisibility(View.VISIBLE);
                }
                else {
                    setEnabledCheckBoxes(true);
                    mTvDate.setVisibility(View.INVISIBLE);
                }
            }
        });
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mTvSetDetails = (EditText) findViewById(R.id.ta_details_textView);

        mDaysToAlarm = "0000000"; // edit bux(1) checkboxes are incorrect while editing

        mCheckBoxesArr = new ArrayList<CheckBox>();
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.sunday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.monday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.tuesday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.wednesday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.thursday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.friday));
        mCheckBoxesArr.add((CheckBox) findViewById(R.id.saturday));

        setEnabledCheckBoxes(false);

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
                setEnabledCheckBoxes(daysToAlarm.charAt(0) == '1');
                mTvDate.setVisibility((daysToAlarm.charAt(0) == '1') ? View.INVISIBLE : View.VISIBLE);
                mSwitchRepeating.setChecked(daysToAlarm.charAt(0) == '1');
                mDaysToAlarm = daysToAlarm.substring(1);
                initializeCheckBoxes();
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
                if(!checkWithRegExp(mEtTitle.getText().toString(), getString(R.string.title_pttrn))) {
                    initializeDaysOfWeek();
                    startAlarm();
                }
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

    protected void setEnabledCheckBoxes(boolean pIsEnabled){
        for (CheckBox c : mCheckBoxesArr){
            c.setEnabled(pIsEnabled);
        }
    }

    protected void initializeDaysOfWeek(){
        mDaysToAlarm = "";
        for (CheckBox c : mCheckBoxesArr){
            mDaysToAlarm += c.isChecked() ? "1" : "0";
        }
    }

    protected void initializeCheckBoxes(){
        for(int i = 0; i < 7; i++){
            mCheckBoxesArr.get(i).setChecked(mDaysToAlarm.charAt(i) == '1');
        }
    }

    protected void writePlanToDB(Calendar calendar, Plan pPlan) {

        final Plan plan = pPlan;
        plan.setDetails(mTvSetDetails.getText().toString());
        plan.setTitle(mEtTitle.getText().toString());
        plan.setTimeStamp(calendar.getTimeInMillis());
        plan.setImagePath(mSelectedImagePath);
        plan.setDaysToAlarm((mSwitchRepeating.isChecked() ? "1" : "0") + mDaysToAlarm);  //DOW
        plan.setUpdatedAtParseTime(mUpdatedAtParseTime);
        plan.setIsDeleted(0);
        plan.setIsSynchronized((plan.getIsRemote() == 1) ? 1 : 0);

        if (mIsEdit) {
            plan.setParseId(dbManager.getPlanById(mPlanId).getParseId());
            plan.setLocalId(dbManager.getPlanById(mPlanId).getLocalId());
            dbManager.editPlan(plan, mPlanId);
        } else {
            dbManager.saveNewPlan(plan);
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
            case REQUEST_TAKE_PHOTO:
                setImageFromPhoto(returnedIntent);
                break;
        }
    }

    protected void setImageFromPhoto(Intent imageIntent){
//        Bundle extras = imageIntent.getExtras();
//        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        mIvImage.setImageBitmap(imageBitmap);
        int targetW = mIvImage.getWidth();
        int targetH = mIvImage.getHeight();
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
        mIvImage.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mSelectedImagePath = image.getAbsolutePath();
        return image;
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
        if (!mIsEdit && !SystemUtils.isKitKatHigher()) {
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

        if (!mSwitchRepeating.isChecked() && System.currentTimeMillis() > mMyLovelyCalendar.getTimeInMillis()) {
            showToast("Time is incorrect.");
            return false;
        }
        else if(mSwitchRepeating.isChecked() && mMyLovelyCalendar.getTimeInMillis() < System.currentTimeMillis()){
            fixCalendarForRepeat();
        }

        return true;
    }

    protected void fixCalendarForRepeat(){
        Calendar calendar = Calendar.getInstance();

        mMyLovelyCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        mMyLovelyCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        mMyLovelyCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
    }

    protected void fillIn(Plan p) {
        mEtTitle.setText(p.getTitle());
        mTvSetDetails.setText(p.getDetails());
        mMyLovelyCalendar = DataUtils.getCalendarByTimeStamp(p.getTimeStamp());
        mTvDate.setText(DataUtils.getDateStrFromCalendar(mMyLovelyCalendar));
        mTvTime.setText(DataUtils.getTimeStrFromCalendar(mMyLovelyCalendar));
        mUpdatedAtParseTime = p.getUpdatedAtParseTime();

        mDaysToAlarm = p.getDaysToAlarm().substring(1);
        initializeCheckBoxes();
        mSelectedImagePath = p.getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath);
        mIvImage.setImageBitmap(bitmap);
        if (mSelectedImagePath == null) {
            mIvImage.setImageResource(R.drawable.aa_icon);
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
                case R.id.ta_planImage_imageView:
                    ChooseImageDialog dialog = new ChooseImageDialog();
                    dialog.setListener(new ChooseImageDialogClicker());
                    dialog.show(getSupportFragmentManager(), "imagePicker");
                    break;

            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
              showToast("The file hasn't been created");
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    protected final class ChooseImageDialogClicker implements ChooseImageDialog.ChooseImageDialogListener{

        @Override
        public void onChooseFromGalleryClick(){
            chooseImage();
        }

        @Override
        public void onTakePictureClick(){
            dispatchTakePictureIntent();
        }
    }
    protected final class DialogClicker implements DaysOfWeekDialog.DaysOfWeekDialogListener,
            DatePickDialog.DatePickListener, TimePickDialog.TimePickListener {

        @Override
        public void onDaysOfWeekPositiveClick(String pDaysOfWeek) {
            mDaysToAlarm = pDaysOfWeek; //DOW
            initializeCheckBoxes();
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    private final class DeletePlanDialogClicker implements TwoButtonsAlertDialog.IAlertDialog{

        @Override
        public void onAcceptClick() {
            TaskActivity.super.onBackPressed();
        }


    }

    @Override
    public void onBackPressed() {
        TwoButtonsAlertDialog dialDelPlan = new TwoButtonsAlertDialog();
        dialDelPlan.setIAlertDialog(new DeletePlanDialogClicker());
        dialDelPlan.setDialogTitle("All unsaved data will be lost. Continue?");
        dialDelPlan.setPositiveBtnText("Yes");
        dialDelPlan.setNegativeBtnText("Cancel");
        dialDelPlan.show(getFragmentManager(), getString(R.string.pa_delete_plan));
    }
}
