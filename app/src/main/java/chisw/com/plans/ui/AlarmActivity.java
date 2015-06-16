package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import chisw.com.plans.R;

/**
 * Created by Yuriy on 15.06.2015.
 */
public class AlarmActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Clicker c = new Clicker();
        findViewById(R.id.bt_ret).setOnClickListener(c);
    }

    public static void start(Activity a) {
        Intent i = new Intent(a, AlarmActivity.class);
        a.startActivity(i);
        //activity.startActivity(new Intent(activity, AlarmActivity.class));
    }

    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.bt_ret:
                    finish();
                    break;
            }
        }
    }
}
