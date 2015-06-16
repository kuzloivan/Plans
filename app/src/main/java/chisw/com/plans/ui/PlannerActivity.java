package chisw.com.plans.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import chisw.com.plans.R;

/**
 * Created by Alexander on 15.06.2015.
 */
public class PlannerActivity extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.title_activity_planner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        Clicker clicker = new Clicker();

        findViewById(R.id.pa_goBack_btn).setOnClickListener(clicker);
    }



    @Override
    protected int contentViewResId() {
        return R.layout.activity_planner;
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
