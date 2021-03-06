package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

import chisw.com.dayit.R;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.utils.DataUtils;

public class LocalTaskActivity extends TaskActivity {
    private static final String BUNDLE_ID_KEY = "chisw.com.DayIt.ui.activities.localTask_activity.id";
    private static final String BUNDLE_KEY = "chisw.com.DayIt.ui.activities.localTask_activity.bundle";
    private final int REQUEST_AUDIO_GET = 1;

    private TextView mAlarmSoundName;
    private int mDurationBuf;
    private long mAudioDuration;
    private String mAudioPath;
    private SeekBar mSeekBar;
    private RelativeLayout mAudioLayout;
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
        findViewById(R.id.sunday).setOnClickListener(mLClicker);
        findViewById(R.id.monday).setOnClickListener(mLClicker);
        findViewById(R.id.tuesday).setOnClickListener(mLClicker);
        findViewById(R.id.wednesday).setOnClickListener(mLClicker);
        findViewById(R.id.thursday).setOnClickListener(mLClicker);
        findViewById(R.id.friday).setOnClickListener(mLClicker);
        findViewById(R.id.saturday).setOnClickListener(mLClicker);

        mTvDate.setOnClickListener(mLClicker);
        mTvTime.setOnClickListener(mLClicker);
        mSwitchRepeating.setOnClickListener(mLClicker);
        mIvImage.setOnClickListener(mLClicker);

        mTvSoundDuration = (TextView) findViewById(R.id.tv_sound_duration);
        mTvSoundDuration.setOnClickListener(mLClicker);
        SeekBarListener sb = new SeekBarListener();

        mAudioLayout = (RelativeLayout) findViewById(R.id.lta_audio_layout);
        mAudioLayout.setVisibility(View.GONE);
        mAlarmSoundName = (TextView) findViewById(R.id.lta_alarmSoundName_textView);
        mSeekBar = (SeekBar) findViewById(R.id.lta_soundDuration_seekBar);
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
                mAudioLayout.setVisibility(View.VISIBLE);
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
        if (!super.checkFields()) {
            return;
        }
        writePlanToDB(mMyLovelyCalendar);
        super.startAlarm();
    }

    private void writePlanToDB(Calendar calendar) {
        Plan p = new Plan();
        p.setAudioPath(mAudioPath);
        p.setAudioDuration((int) mAudioDuration);
        p.setIsRemote(0);
        p.setPlanState(Plan.PLAN_STATE_LOCAL);
        super.writePlanToDB(calendar, p);
    }

    private void setAudioFromSDCard(Intent audioIntent) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(getContentResolver().getType(audioIntent.getData()));
        String typ = mime.getMimeTypeFromExtension(type);

        mIsDialogExist = false;

        if (!"audio/mpeg".equalsIgnoreCase(typ)) {
            showToast("File is not valid");
            return;
        }

        Uri u = audioIntent.getData();
        mAudioPath = getPath(u);
        mDurationBuf = getAudioDuration(u);
        mAlarmSoundName.setText(getName(u, mAudioPath));
        duration();
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

    private void duration() {
        mAudioDuration = (mDurationBuf * mSeekBar.getProgress()) / 100;
        timeFormat();
    }

    private void timeFormat() {
        mTvSoundDuration.setText(DataUtils.getTimeStrFromTimeStamp((int) mAudioDuration));
    }

    private void fillIn(SeekBar seekbar) {
        Plan p = dbManager.getPlanById(mPlanId);
        super.fillIn(p);
        mAudioPath = p.getAudioPath();
        if (mAudioPath == null) {
            seekbar.setEnabled(false);
            mAudioLayout.setVisibility(View.GONE);
            return;
        }
        mAudioLayout.setVisibility(View.VISIBLE);
        seekbar.setEnabled(true);
        Uri u = Uri.parse(mAudioPath);
        mAudioDuration = p.getAudioDuration();
        mDurationBuf = getAudioDuration(u);
        Uri tmpUri = Uri.parse(mAudioPath);
        mAlarmSoundName.setText(getName(tmpUri, mAudioPath));
        seekbar.setProgress(getPercent((int) mAudioDuration, mAudioPath));
        timeFormat();
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
            duration();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            duration();
        }
    }
}
