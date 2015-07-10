package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

import chisw.com.dayit.R;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public class LocalTaskActivity extends TaskActivity {
    private static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.localTask_activity.id";
    private static final String BUNDLE_KEY = "chisw.com.plans.ui.activities.localTask_activity.bundle";
    private final int REQUEST_AUDIO_GET = 1;

    private TextView mAlarmSoundTitle;
    private int mDurationBuf;
    private long mAudioDuration;
    private String mAudioPath;
    private SeekBar mSeekBar;
    private RelativeLayout mRelativeLayoutDuration;
    private LClicker mLClicker;

    public static void start(Activity a) {
        Intent i = new Intent(a, LocalTaskActivity.class);
        a.startActivity(i);
    }

    public static void start(Activity a, int id) {
        Intent i = new Intent(a, LocalTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, id);
        i.putExtra(BUNDLE_KEY, bundle);
        a.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        if (getIntent().hasExtra(BUNDLE_KEY)) {
            mIsEdit = true;
            mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
            fillIn(mSeekBar);
        }
        dateFillIn();
    }

    @Override
    protected void initViews() {
        super.initViews();

        mLClicker = new LClicker();
        findViewById(R.id.lta_setAudio_btn).setOnClickListener(mLClicker);

        mTvDate.setOnClickListener(mLClicker);
        mTvTime.setOnClickListener(mLClicker);
        mSwitchRepeating.setOnClickListener(mLClicker);
        mIvImage.setOnClickListener(mLClicker);

        mTvSoundDuration = (TextView) findViewById(R.id.tv_sound_duration);
        mTvSoundDuration.setOnClickListener(mLClicker);
        SeekBarListener sb = new SeekBarListener();

        mRelativeLayoutDuration = (RelativeLayout) findViewById(R.id.lta_audio_layout);
        mAlarmSoundTitle = (TextView) findViewById(R.id.alarmSoundTitle_textview);
        mSeekBar = (SeekBar) findViewById(R.id.sb_duration_sound);
        mSeekBar.setOnSeekBarChangeListener(sb);
        mSeekBar.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        if (resultCode != RESULT_OK) {
            mIsDialogExist = false;
            return;
        }
        mSeekBar.setEnabled(true);
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                setAudioFromSDCard(returnedIntent);
                mRelativeLayoutDuration.setVisibility(View.VISIBLE);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, returnedIntent);
                break;
        }
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_local_task;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void startAlarm() {
        writePlanToDB(mMyLovelyCalendar);
        super.startAlarm();
    }

    private void writePlanToDB(Calendar calendar) {
        if(!super.checkFields())
        {
            return;
        }
        Plan p = new Plan();
        p.setAudioPath(mAudioPath);
        p.setAudioDuration((int) mAudioDuration);
        p.setIsRemote(0);
        super.writePlanToDB(calendar, p);
    }

    private void setAudioFromSDCard(Intent audioIntent) {
        mAudioPath = getPath(audioIntent);
        mIsDialogExist = false;
        String buf;
        Uri u = audioIntent.getData();
        buf = getName(u, mAudioPath);
        if (!ValidData.isValidFormat(buf)) {
            mTvSoundDuration.setText("00:00");
            mAlarmSoundTitle.setText("");
            showToast("File is not valid");
            return;
        }
        if (SystemUtils.isKitKatHigher()) {
            mDurationBuf = getAudioDuration(audioIntent.getData());
        } else {
            mDurationBuf = getAudioDuration(u);
        }
        mAlarmSoundTitle.setText(buf);
        duration(mSeekBar);
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

    private int getPercent(int val, String path) {
        Uri u = Uri.parse(path);
        return (val * 100) / getAudioDuration(u);
    }

    private int getAudioDuration(Uri path) {
        MediaMetadataRetriever mm = new MediaMetadataRetriever();
        mm.setDataSource(this, path);
        int durationMs = Integer.parseInt(mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        return durationMs / 1000;
    }

    private void duration(SeekBar seekBar) {
        mAudioDuration = (mDurationBuf * seekBar.getProgress()) / 100;
        timeFormat();
    }

    private String getPath(Intent strIntent) {
        Uri data = strIntent.getData();
        if (!SystemUtils.isKitKatHigher() || !DocumentsContract.isDocumentUri(this, data)) {
            return strIntent.getDataString();
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
        return getDataColumn(contentUri, selection, selectionArgs);
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs,
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

    private void timeFormat() {
        mTvSoundDuration.setText(DataUtils.getTimeStrFromTimeStamp((int) mAudioDuration));
    }

    private void fillIn(SeekBar seekbar) {
        Plan p = dbManager.getPlanById(mPlanId);
        mAudioPath = p.getAudioPath();
        if (mAudioPath == null) {
            seekbar.setEnabled(false);
            mRelativeLayoutDuration.setVisibility(View.GONE);
            return;
        }
        mRelativeLayoutDuration.setVisibility(View.VISIBLE);
        seekbar.setEnabled(true);
        Uri u = Uri.parse(mAudioPath);
        mAudioDuration = p.getAudioDuration();
        mDurationBuf = getAudioDuration(u);
        Uri tmpUri = Uri.parse(mAudioPath);
        mAlarmSoundTitle.setText(getName(tmpUri, mAudioPath));
        seekbar.setProgress(getPercent((int) mAudioDuration, mAudioPath));
        timeFormat();
        super.fillIn(p);
    }

    private final class LClicker extends TaskActivity.Clicker {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lta_setAudio_btn:
                    chooseAudio();
                    break;
                default:
                    super.onClick(v);
            }
        }
    }

    private final class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

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

}
