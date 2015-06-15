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
        setContentView(R.layout.splash_main);

        Clicker clicker = new Clicker();

        btnOpenPlannerActivity = (Button)findViewById(R.id.btnOpenPlannerActivity);
        btnOpenPlannerActivity.setOnClickListener(clicker);

        mediaBt = (Button) findViewById(R.id.mediaBt);


        alarmBt = (Button) findViewById(R.id.alarmBt);
        alarmBt.setOnClickListener(clicker);

        settingsBt = (Button) findViewById(R.id.settingsBt);
        settingsBt.setOnClickListener(clicker);
    }

    public final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.alarmBt:
                    AlarmActivity.start(SplashActivity.this);
                    break;

                case R.id.btnOpenPlannerActivity:
                    PlannerActivity.start(SplashActivity.this);
                    break;
                case R.id.settingsBt:
                    SettingsActivity.start(SplashActivity.this);
                    break;
            }
        }
    }



}
