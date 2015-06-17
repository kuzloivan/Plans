package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.adapters.PlannerArrayAdapter;

/**
 * Created by Alexander on 15.06.2015.
 */
public class PlannerActivity extends ToolbarActivity {

    ListView lvPlanner;
    PlannerArrayAdapter plannerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackButton();

        Clicker clicker = new Clicker();

        lvPlanner = (ListView)findViewById(R.id.pa_planner_listview);

        plannerArrayAdapter = new PlannerArrayAdapter(this, dbManager.getAllPlans());
        lvPlanner.setAdapter(plannerArrayAdapter);

        Plan p = new Plan();
        p.setTitle("Make it!");
        p.setTimeStamp(1234123);
        dbManager.saveNewPlan(p);

        plannerArrayAdapter.notifyDataSetChanged();
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
            case R.id.pa_menu_add_reminder:
                AlarmActivity.start(PlannerActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
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
