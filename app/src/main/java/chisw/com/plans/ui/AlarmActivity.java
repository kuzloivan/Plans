package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import chisw.com.plans.R;

/**
 * Created by Yuriy on 15.06.2015.
 */
public class AlarmActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm );
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AlarmActivity.class));
    }
}
