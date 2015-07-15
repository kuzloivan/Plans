package chisw.com.dayit.ui.activities;

import android.os.Bundle;

import chisw.com.dayit.R;

/**
 * Created by Kuzlo on 15.07.2015.
 */

public class UserActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle pSavedInstanceState){
        super.onCreate(pSavedInstanceState);

    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_user;
    }

}
