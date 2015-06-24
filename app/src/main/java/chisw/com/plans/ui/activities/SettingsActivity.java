package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import chisw.com.plans.R;


public class SettingsActivity extends ToolbarActivity {
    private boolean chooseVibration;
    private boolean chooseNotification;
    private boolean chooseSynchronization;

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
        CompoundButton cbNotification = (CompoundButton) findViewById(R.id.sa_notification_switch);
        CompoundButton cbVibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);
        CompoundButton cbSynchronization = (CompoundButton) findViewById(R.id.sa_autoSync_switch);

        if (sharedHelper.getVibrationOn()) {
            cbVibration.setChecked(true);
        } else {
            cbVibration.setChecked(false);
        }

        if (sharedHelper.getNotificationOn()) {
            cbNotification.setChecked(true);
        } else {
            cbNotification.setChecked(false);
        }

        if (sharedHelper.getSynchronization()) {
            cbSynchronization.setChecked(true);
        } else {
            cbSynchronization.setChecked(false);
        }

        cbNotification.setOnCheckedChangeListener(sw);
        cbVibration.setOnCheckedChangeListener(sw);
        cbSynchronization.setOnCheckedChangeListener(sw);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseNotification = sharedHelper.getNotificationOn();
        chooseVibration = sharedHelper.getVibrationOn();
        chooseSynchronization = sharedHelper.getSynchronization();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedHelper.setVibrationOn(chooseVibration);
        sharedHelper.setNotificationOn(chooseNotification);
        sharedHelper.setSynchronization(chooseSynchronization);
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
        public void onCheckedChanged(CompoundButton cb, boolean cbBool) {
            switch (cb.getId()) {

                case R.id.sa_vibration_switch:
                    chooseVibration = cbBool;
                    showToast("choose_vibration = " + chooseVibration);
                    break;
                case R.id.sa_notification_switch:
                    chooseNotification = cbBool;
                    showToast("choose_notification = " + chooseNotification);
                    break;
                case R.id.sa_autoSync_switch:
                    chooseSynchronization = cbBool;
                    showToast("chooseSynchronization = " + chooseSynchronization);
                    break;
            }
        }
    }
}

