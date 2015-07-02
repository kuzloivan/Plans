package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import chisw.com.plans.R;

/**
 * Created by Oksana on 30.06.2015.
 */
public class SignUpActivity extends ToolbarActivity {

    private EditText mLogin;
    private EditText mPassword;
    private Clicker mClicker;

    public static void start(Activity a) {
        Intent intent = new Intent(a, SignUpActivity.class);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_sign_up;
    }

    private void initView() {
        mClicker = new Clicker();
        mLogin = (EditText) findViewById(R.id.new_user_login);
        mPassword = (EditText) findViewById(R.id.new_user_password);
        findViewById(R.id.btn_sign_up).setOnClickListener(mClicker);
        findViewById(R.id.btn_back_to_log_in).setOnClickListener(mClicker);
    }

    public final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sign_up:
                    break;
                case R.id.btn_back_to_log_in:
                    LogInActivity.start(SignUpActivity.this);
                    break;
            }
        }
    }
}
