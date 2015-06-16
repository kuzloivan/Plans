package chisw.com.plans.ui.activities;

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
        findViewById(R.id.sa_net_btn).setOnClickListener(clicker);
        findViewById(R.id.sa_alarm_btn).setOnClickListener(clicker);
        findViewById(R.id.sa_settings_btn).setOnClickListener(clicker);
    }


    public final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sa_alarm_btn:
                    AlarmActivity.start(SplashActivity.this);
                    break;

                case R.id.sa_net_btn:
                    NetManagementActivity.start(SplashActivity.this);
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
