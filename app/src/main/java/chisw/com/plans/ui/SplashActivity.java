package chisw.com.plans.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import chisw.com.plans.R;

public class SplashActivity extends Activity {
    private Button mediaBt;
    private Button netMngtBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        mediaBt = (Button) findViewById(R.id.mediaBt);
        netMngtBtn = (Button) findViewById(R.id.netMngtStart_btn);
    }






}
