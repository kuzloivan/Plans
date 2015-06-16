package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.ui.adapters.PlannerArrayAdapter;

/**
 * Created by Alexander on 15.06.2015.
 */
public class PlannerActivity extends ToolbarActivity {

    ListView lvPlanner;
    PlannerArrayAdapter plannerArrayAdapter;
    List<String[]> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.title_activity_planner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Clicker clicker = new Clicker();

        lvPlanner = (ListView)findViewById(R.id.pa_planner_listview);

        arrayList = new ArrayList<>();
        plannerArrayAdapter = new PlannerArrayAdapter(this, arrayList);
        lvPlanner.setAdapter(plannerArrayAdapter);

        arrayList.add(new String[]{"Make tea", "16.00", "Tomorrow"});
        arrayList.add(new String[] {"Make coffee", "15.00", "Today"});

        lvPlanner.deferNotifyDataSetChanged();

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
