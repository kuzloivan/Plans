package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.adapters.PlannerCursorAdapter;

public class PlannerActivity extends ToolbarActivity {

    ListView lvPlanner;
    PlannerCursorAdapter plannerCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackButton();

        Clicker clicker = new Clicker();

        lvPlanner = (ListView)findViewById(R.id.pa_planner_listview);

        plannerCursorAdapter = new PlannerCursorAdapter(this);

        lvPlanner.setAdapter(plannerCursorAdapter);

        Plan p = new Plan();
        p.setTitle("Make it!");
        p.setTimeStamp(Calendar.getInstance().getTimeInMillis());
        dbManager.saveNewPlan(p);

        Cursor cursor = dbManager.getPlans();

        if(cursor.moveToFirst()){
            plannerCursorAdapter.swapCursor(cursor);
        }
        else {
            showToast("Error moving cursor to first element");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    int counter = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pa_menu_splash:
                SplashActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_add_reminder:
                AlarmActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_settings:
                SettingsActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_media:
                MediaActivity.start(PlannerActivity.this);
                break;            

            case R.id.pa_menu_log_off:
                //Log off!
                showProgressDialog("Loging Off", "Please, wait...");
                netManager.logoutUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogOut());
                
                LogInActivity.start(PlannerActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public final class CallbackLogOut implements LogOutCallback
    {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                showToast(e.getMessage());
                hideProgressDialog();
                return;
            }
            dbManager.clearPlans();
            dbManager.eraseMe(sharedHelper.getDefaultLogin());
            sharedHelper.clearData();
            hideProgressDialog();
            showToast("Successful");
        }
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

            }
        }
    }
}
