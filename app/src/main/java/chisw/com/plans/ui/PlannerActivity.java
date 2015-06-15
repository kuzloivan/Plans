package chisw.com.plans.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import chisw.com.plans.R;

/**
 * Created by Alexander on 15.06.2015.
 */
public class PlannerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        Clicker clicker = new Clicker();

        findViewById(R.id.pa_goBack_btn).setOnClickListener(clicker);

        //getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public static void start(Activity activity){
        Intent intent = new Intent(activity, PlannerActivity.class);
        activity.startActivity(intent);
    }

    public final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pa_goBack_btn:
                    Toast.makeText(getApplicationContext(), "We are gone...", Toast.LENGTH_SHORT).show();
                    PlannerActivity.this.finish();
                    break;
            }
        }
    }
}
