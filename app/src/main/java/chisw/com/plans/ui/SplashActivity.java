package chisw.com.plans.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import chisw.com.plans.R;

public class SplashActivity extends Activity {
    private Button mediaBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        mediaBt = (Button) findViewById(R.id.mediaBt);
    }






}
