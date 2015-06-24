package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import chisw.com.plans.R;
import chisw.com.plans.db.Mapper;
import chisw.com.plans.model.Plan;
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
    protected void onResume() {
        super.onResume();

        int id = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);

        Plan plan = dbManager.getPlanById(id);

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
