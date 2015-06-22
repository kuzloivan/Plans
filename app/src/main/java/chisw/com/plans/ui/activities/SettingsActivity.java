package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import chisw.com.plans.R;


public class SettingsActivity extends ToolbarActivity {
    public static boolean Choose_user_audio = false;
    public static boolean Choose_notif_vibration = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switcher sw = new Switcher();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CompoundButton cb_user_sound = (CompoundButton) findViewById(R.id.sa_set_user_sound);
        CompoundButton cb_vibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);
        if (Choose_user_audio){
            cb_user_sound.setChecked(true);
        }
        else {
            cb_user_sound.setChecked(false);
        }
        if (Choose_notif_vibration){
            cb_vibration.setChecked(true);
        }
        else {
            cb_vibration.setChecked(false);
        }

        cb_user_sound.setOnCheckedChangeListener(sw);
        cb_vibration.setOnCheckedChangeListener(sw);

  
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

    

    public final class Switcher implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton cb, boolean cb_bool){
            switch (cb.getId()) {
                case R.id.sa_set_user_sound:
                    Choose_user_audio = cb_bool;
                    showToast("Choose_user_audio = " + Choose_user_audio);
                    break;
                case R.id.sa_vibration_switch:
                    Choose_notif_vibration = cb_bool;
                    showToast("Choose_notif_vibration = " + Choose_notif_vibration);
                    break;
            }
        }
    }

}

