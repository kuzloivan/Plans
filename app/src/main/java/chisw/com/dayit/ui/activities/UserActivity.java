package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import chisw.com.dayit.R;

/**
 * Created by Kuzlo on 15.07.2015.
 */

public class UserActivity extends ToolbarActivity {

    private TextView mLoginTV;
    private TextView mPhoneTV;

    @Override
    protected void onCreate(Bundle pSavedInstanceState){
        super.onCreate(pSavedInstanceState);
        initViews();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_user;
    }

    private void initViews(){
        initBackButton();
        mLoginTV = (TextView)findViewById(R.id.ua_login_tv);
        mPhoneTV = (TextView) findViewById(R.id.ua_phone_number_tv);

        mLoginTV.setText(sharedHelper.getDefaultLogin());
        mPhoneTV.setText(sharedHelper.getDefaultPhone());
    }
}
