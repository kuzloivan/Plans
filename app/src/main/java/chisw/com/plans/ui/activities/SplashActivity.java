package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import chisw.com.plans.R;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Clicker clicker = new Clicker();

        findViewById(R.id.sa_planner_btn).setOnClickListener(clicker);
        findViewById(R.id.sa_media_btn).setOnClickListener(clicker);
        findViewById(R.id.sa_alarm_btn).setOnClickListener(clicker);
        findViewById(R.id.sa_settings_btn).setOnClickListener(clicker);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }

    public final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sa_alarm_btn:
                    AlarmActivity.start(SplashActivity.this);
                    break;

                case R.id.sa_planner_btn:
                    PlannerActivity.start(SplashActivity.this);
                    break;

                case R.id.sa_settings_btn:
                    SettingsActivity.start(SplashActivity.this);
                    break;

                case R.id.sa_media_btn:
                    MediaActivity.start(SplashActivity.this);
                    break;
            }
        }
    }
}
