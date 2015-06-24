package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import chisw.com.plans.R;
import chisw.com.plans.db.Mapper;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.adapters.PlannerCursorAdapter;
import chisw.com.plans.utils.DataUtils;

/**
 * Created by Alexander on 20.06.2015.
 */
public class ViewPlanActivity extends ToolbarActivity {

    public static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.view_plan_activity.id";
    public static final String BUNDLE_KEY = "chisw.com.plans.ui.activities.view_plan_activity.bundle";

    TextView pv_tv_time;
    TextView pv_tv_date;
    TextView pv_tv_details;

    private int planId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getIntent().getBundleExtra();
        pv_tv_time = (TextView)findViewById(R.id.pv_tv_time);
        pv_tv_date = (TextView)findViewById(R.id.pv_tv_date);
        pv_tv_details = (TextView)findViewById(R.id.pv_tv_details);

        initBackButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_plan, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.vp_menu_delete:

                deleteEntirely(dbManager.getPlanById(planId), planId);


                break;
            case R.id.vp_menu_edit:

                Plan p = dbManager.getPlanById(planId);
                AlarmActivity.start(this, p.getLocalId());

                break;
        }

        finish();

        return super.onOptionsItemSelected(item);
    }

    @Deprecated
    public void deleteEntirely(Plan plan, int idIndex){
        alarmManager.cancelAlarm(plan);

        if(!sharedHelper.getSynchronization()){
            synchronization.wasDeleting((dbManager.getPlanById(idIndex).getLocalId()));
        }
        else{
            netManager.deletePlan((dbManager.getPlanById(idIndex).getParseId()));
        }

        dbManager.deletePlanById(idIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();

        planId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);

        Plan plan = dbManager.getPlanById(planId);

        setTitle(plan.getTitle());
        pv_tv_time.setText(DataUtils.getTimeStringFromTimeStamp(plan.getTimeStamp()));
        pv_tv_date.setText(DataUtils.getDateStringFromTimeStamp(plan.getTimeStamp()));
        pv_tv_details.setText(plan.getDetails());
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }


    public static void start(Activity activity, int id) {
        Intent intent = new Intent(activity, ViewPlanActivity.class);

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_ID_KEY, id);

        intent.putExtra(BUNDLE_KEY, bundle);

        activity.startActivity(intent);
    }


}
