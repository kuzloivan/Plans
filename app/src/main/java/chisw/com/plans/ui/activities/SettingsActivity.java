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


    public boolean isChoose_notification() {
        return choose_notification;
    }

    public void setChoose_notification(boolean choose_notification) {
        this.choose_notification = choose_notification;
    }

    public boolean isChoose_vibration() {
        return choose_vibration;
    }

    public void setChoose_vibration(boolean choose_vibration) {
        this.choose_vibration = choose_vibration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switcher sw = new Switcher();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CompoundButton cb_notification = (CompoundButton) findViewById(R.id.sa_notification_switch);
        CompoundButton cb_vibration = (CompoundButton) findViewById(R.id.sa_vibration_switch);
        CompoundButton cb_user_sound = (CompoundButton) findViewById(R.id.sa_set_user_sound);

        setChoose_vibration(true);
        setChoose_notification(true);

        cb_notification.setChecked(isChoose_notification());
        cb_vibration.setChecked(isChoose_vibration());
        cb_user_sound.setChecked(false);

        cb_user_sound.setOnCheckedChangeListener(sw);
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


    public final class Switcher implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton cb, boolean cb_bool){
            switch (cb.getId()) {

                case R.id.sa_vibration_switch:
                    choose_vibration = cb_bool;
                    showToast("choose_vibration = " + isChoose_vibration());
                    break;
                case R.id.sa_notification_switch:
                    choose_notification = cb_bool;
                    showToast("choose_notification = " + isChoose_notification());
                    break;
            }
        }
    }

}

