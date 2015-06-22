package chisw.com.plans.ui.activities;

import android.os.Bundle;

/**
 * Created by Alexander on 20.06.2015.
 */
public class ViewPlanActivity extends ToolbarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackButton();
    }

    @Override
    protected int contentViewResId() {
        return 0;
    }
}
