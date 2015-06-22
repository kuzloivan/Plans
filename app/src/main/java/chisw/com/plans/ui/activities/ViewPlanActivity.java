package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import chisw.com.plans.R;

/**
 * Created by Alexander on 20.06.2015.
 */
public class ViewPlanActivity extends ToolbarActivity {

    public static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.view_plan_activity.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getIntent().getBundleExtra();

        initBackButton();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }


    public static void start(Activity activity, int id) {
        Intent intent = new Intent(activity, ViewPlanActivity.class);

        Bundle bundle = new Bundle();
        intent.putExtra("bundle", bundle);

        bundle.putInt(BUNDLE_ID_KEY, id);

        activity.startActivity(intent);
    }


}
