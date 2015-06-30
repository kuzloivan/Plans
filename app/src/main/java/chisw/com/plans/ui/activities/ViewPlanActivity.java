package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import chisw.com.plans.R;
import chisw.com.plans.model.Plan;
import chisw.com.plans.utils.DataUtils;

/**
 * Created by Alexander on 20.06.2015.
 */
public class ViewPlanActivity extends ToolbarActivity {

    public static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.view_plan_activity.id";
    public static final String BUNDLE_KEY = "chisw.com.plans.ui.activities.view_plan_activity.bundle";

    private TextView mTv_time;
    private TextView mTv_date;
    private TextView mTv_details;
    private Plan mPlan;
    private int mPlanId;

    public static void start(Activity pActivity, int pId) {
        Intent intent = new Intent(pActivity, ViewPlanActivity.class);

        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_ID_KEY, pId);

        intent.putExtra(BUNDLE_KEY, bundle);

        pActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
        initBackButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        getMenuInflater().inflate(R.menu.menu_view_plan, pMenu);
        return super.onCreateOptionsMenu(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {

        switch (pMenuItem.getItemId()) {
            case R.id.vp_menu_delete:
                deleteEntirely(mPlan, mPlanId);
                break;
            case R.id.vp_menu_edit:
                AlarmActivity.start(this, mPlanId);
                break;
        }
        finish();
        return super.onOptionsItemSelected(pMenuItem);
    }

    @Deprecated
    public void deleteEntirely(Plan pPlan, int pIdIndex) {
        alarmManager.cancelAlarm(pPlan);
        if (!sharedHelper.getSynchronization()) {
            synchronization.wasDeleting(mPlanId);
        } else {
            netManager.deletePlan((pPlan.getParseId()));
        }
        dbManager.deletePlanById(pIdIndex);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }

    private void initView() {
        mTv_time = (TextView) findViewById(R.id.pv_tv_time);
        mTv_date = (TextView) findViewById(R.id.pv_tv_date);
        mTv_details = (TextView) findViewById(R.id.pv_tv_details);
        mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        mPlan = dbManager.getPlanById(mPlanId);
        setTitle(mPlan.getTitle());
        mTv_time.setText(DataUtils.getTimeStringFromTimeStamp(mPlan.getTimeStamp()));
        mTv_date.setText(DataUtils.getDateStringFromTimeStamp(mPlan.getTimeStamp()));
        mTv_details.setText(mPlan.getDetails());
    }


}
