package chisw.com.plans.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chisw.com.plans.R;

public class SplashActivity extends Activity {

    private Button mediaBt;
    private Button alarmBt;
    private Button plannerBt;
    private Button settingsBt;
    private Button net_managerBt;
    private Button btnOpenPlannerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Clicker clicker = new Clicker();

        btnOpenPlannerActivity = (Button)findViewById(R.id.sa_planner_btn);
        btnOpenPlannerActivity.setOnClickListener(clicker);

        mediaBt = (Button) findViewById(R.id.sa_media_btn);
        mediaBt.setOnClickListener(clicker);

        net_managerBt = (Button) findViewById(R.id.sa_net_btn);
        net_managerBt.setOnClickListener(clicker);

        alarmBt = (Button) findViewById(R.id.sa_alarm_btn);
        alarmBt.setOnClickListener(clicker);

        settingsBt = (Button) findViewById(R.id.sa_settings_btn);
        settingsBt.setOnClickListener(clicker);
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
