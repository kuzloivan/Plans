package chisw.com.plans.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chisw.com.plans.R;

public class SplashActivity extends Activity {
    private Button mediaBt;
    private Button netMngtBtn;
    private Button alarmBt;
    private Button plannerBt;
    private Button settingBt;
    private Button net_managerBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        Clicker clicker = new Clicker();

        mediaBt = (Button) findViewById(R.id.mediaBt);
        netMngtBtn = (Button) findViewById(R.id.netMngtStart_btn);


        alarmBt = (Button) findViewById(R.id.alarmBt);
        alarmBt.setOnClickListener(clicker);
    }

    public final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.alarmBt:
                    AlarmActivity.start(SplashActivity.this);
                    break;

            }
        }
    }



}
