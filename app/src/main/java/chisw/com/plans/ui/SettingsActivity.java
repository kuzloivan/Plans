package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import chisw.com.plans.R;

public class SettingsActivity extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Clicker clicker = new Clicker();
        findViewById(R.id.sa_ret_btn).setOnClickListener(clicker);
    }

    @Override
    protected int contentViewResId() {
       return R.layout.activity_settings;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.sa_ret_btn:
                    finish();
                    break;
            }
        }
    }


}

