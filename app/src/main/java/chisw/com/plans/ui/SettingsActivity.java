package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import chisw.com.plans.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}

