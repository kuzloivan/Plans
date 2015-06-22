package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import chisw.com.plans.R;
import chisw.com.plans.core.SharedHelper;


public class SettingsActivity extends ToolbarActivity {
    private boolean choose_vibration;
    private boolean choose_notification;

//    public boolean isChoose_notification() {
//        return choose_notification;
//    }
//
//    public void setChoose_notification(boolean choose_notification) {
//        this.choose_notification = choose_notification;
//    }
//
//    public boolean isChoose_vibration() {
//        return choose_vibration;
//    }
//
//    public void setChoose_vibration(boolean choose_vibration) {
//        this.choose_vibration = choose_vibration;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switcher sw = new Switcher();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CompoundButton cb_notification = (CompoundButton) findViewById(R.id.sa_notification_switch);
        CompoundButton cb_vibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);

        if(sharedHelper.getVibrationOn()){
            cb_vibration.setChecked(true);
        }else {
            cb_vibration.setChecked(false);
        }

        if (sharedHelper.getNotificationOn()){
            cb_notification.setChecked(true);
        } else{
            cb_notification.setChecked(false);
        }

        cb_notification.setOnCheckedChangeListener(sw);
        cb_vibration.setOnCheckedChangeListener(sw);
    }

    @Override
    protected void onResume() {
        super.onResume();
        choose_notification = sharedHelper.getNotificationOn();
        choose_vibration = sharedHelper.getVibrationOn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedHelper.setVibrationOn(choose_vibration);
        sharedHelper.setNotificationOn(choose_notification);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_settings;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public final class Switcher implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton cb, boolean cb_bool) {
            switch (cb.getId()) {

                case R.id.sa_vibration_switch:
                    choose_vibration = cb_bool;
                    showToast("choose_vibration = " + choose_vibration);
                    break;
                case R.id.sa_notification_switch:
                    choose_notification = cb_bool;
                    showToast("choose_notification = " + choose_notification);
                    break;
            }
        }
    }
}

