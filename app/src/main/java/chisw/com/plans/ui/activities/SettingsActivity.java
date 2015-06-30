package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import chisw.com.plans.R;


public class SettingsActivity extends ToolbarActivity {
    private CompoundButton mCbNotification;
    private CompoundButton mCbVibration;
    private CompoundButton mCbSynchronization;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackButton();
        mCbNotification = (CompoundButton) findViewById(R.id.sa_notification_switch);
        mCbVibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);
        mCbSynchronization = (CompoundButton) findViewById(R.id.sa_autoSync_switch);

        if(sharedHelper.getVibrationOn()){
            mCbVibration.setChecked(true);
        }else{
            mCbVibration.setChecked(false);
        }

        if(sharedHelper.getNotificationOn()){
            mCbNotification.setChecked(true);
        }else{
            mCbNotification.setChecked(false);
        }

        if(sharedHelper.getSynchronization()){
            mCbSynchronization.setChecked(true);
        }else{
            mCbSynchronization.setChecked(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedHelper.setVibrationOn(mCbVibration.isChecked());
        sharedHelper.setNotificationOn(mCbNotification.isChecked());
        sharedHelper.setSynchronization(mCbSynchronization.isChecked());
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_settings;
    }
}

