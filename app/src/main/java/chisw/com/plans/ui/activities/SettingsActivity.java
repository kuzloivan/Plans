package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import chisw.com.plans.R;
import chisw.com.plans.others.Multimedia;


public class SettingsActivity extends ToolbarActivity {
    public static boolean Coose_user_audio = false;
    public static boolean Coose_notif_vibration = false;
    private TextView mTextValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switcher sw = new Switcher();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CompoundButton cb_user_sound = (CompoundButton) findViewById(R.id.sa_set_user_sound);
        CompoundButton cb_vibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);
        if (Coose_user_audio){
            cb_user_sound.setChecked(true);
        }
        else {
            cb_user_sound.setChecked(false);
        }
        if (Coose_notif_vibration){
            cb_vibration.setChecked(true);
        }
        else {
            cb_vibration.setChecked(false);
        }

        cb_user_sound.setOnCheckedChangeListener(sw);
        cb_vibration.setOnCheckedChangeListener(sw);

        //======Play with seekBar======
        SeekerBar sb = new SeekerBar();
        final SeekBar seekbar = (SeekBar)findViewById(R.id.sb_duration_sound);
        seekbar.setOnSeekBarChangeListener(sb);
        mTextValue = (TextView)findViewById(R.id.tv_show_duration_sound);
        mTextValue.setText("0");
    }

    //============don't delete=========================================

//    protected void onDestroy() {
//        super.onDestroy();
//       showToast("onDestroy");
//    }
//
//    protected void onPause() {
//        super.onPause();
//        showToast("onPause");
//    }
//
//    protected void onRestart() {
//        super.onRestart();
//        showToast("onRestart");
//    }
//
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        showToast("onRestoreInstanceState");
//    }
//
//    protected void onResume() {
//        super.onResume();
//        showToast("onResume");
//    }
//
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //outState.putBoolean();
//        showToast("onSaveInstanceState");
//    }
//
//    protected void onStart() {
//        super.onStart();
//        showToast("onStart");
//    }
//
//    protected void onStop() {
//        super.onStop();
//        showToast("onStop");
//    }

    //=================================================================

    @Override
    protected int contentViewResId() {
        return R.layout.activity_settings;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
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

    public final class Switcher implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton cb, boolean cb_bool){
            switch (cb.getId()) {
                case R.id.sa_set_user_sound:
                    Coose_user_audio = cb_bool;
                    showToast("Coose_user_audio = " + Coose_user_audio);
                    break;
                case R.id.sa_vibration_switch:
                    Coose_notif_vibration = cb_bool;
                    showToast("Coose_notif_vibration = " + Coose_notif_vibration);
                    break;
            }
        }
    }

}

