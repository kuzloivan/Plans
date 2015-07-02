package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import chisw.com.plans.R;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;

public class LogInActivity extends ToolbarActivity {

    private EditText mLogin;
    private EditText mPassword;
    private ClickerNet mClicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mLogin = (EditText) findViewById(R.id.net_user_login);
        mLogin.setSingleLine();
        mPassword = (EditText) findViewById(R.id.net_user_password);
        mPassword.setSingleLine();
        if (ValidData.isTextValid(sharedHelper.getDefaultLogin()))
        {
            if (ValidData.isTextValid(sharedHelper.getDefaultPass())) {
                PlannerActivity.start(LogInActivity.this);
                LogInActivity.this.finish();
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, LogInActivity.class);
        a.startActivity(intent);
    }

    private void initView(){
        mClicker = new ClickerNet();
        findViewById(R.id.btn_to_sign_up).setOnClickListener(mClicker);
        findViewById(R.id.btn_log_in).setOnClickListener(mClicker);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_log_in;
    }

    public final class ClickerNet implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(!SystemUtils.checkNetworkStatus(getApplicationContext()))
            {
                showToast("No internet connection");
                return;
            }
            String login = mLogin.getText().toString().toLowerCase();
            String password = mPassword.getText().toString();
            switch (v.getId()) {
                case R.id.btn_to_sign_up:
                    SignUpActivity.start(LogInActivity.this);
                    break;
                case R.id.btn_log_in:
                    if(!ValidData.isCredentialsValid(login, getString(R.string.login_pttrn))){
                        showToast("Login must be at least 4 characters length.(a-z,A-Z,0-9)");
                        return;
                    }
                    if(!ValidData.isCredentialsValid(password, getString(R.string.pass_pttrn))){
                        showToast("Password must be at least 6 characters length.(a-z,A-Z,0-9)");
                        return;
                    }
                    showProgressDialog("Logging In", "Please, wait...");
                    netManager.loginUser(login, password, new CallbackLogIn());
                    break;
            }
        }
    }

    public final class CallbackLogIn implements LogInCallback {
        String error = "Error";
        @Override
        public void done(ParseUser parseUser, ParseException e) {
            if (e != null) {
                /* Is username already exist */
                switch(e.getCode()) {
                    case ParseException.OBJECT_NOT_FOUND:
                        error = "Unable to log in. Check your username and password";
                        break;
                }
                showToast(error); //test
                hideProgressDialog();
                return;
            }
            sharedHelper.setDefaultLogin(mLogin.getText().toString().toLowerCase());
            sharedHelper.setDefaultPass(mPassword.getText().toString());
            hideProgressDialog();
            showToast("Login was successful");
            PlannerActivity.start(LogInActivity.this);
            LogInActivity.this.finish();
        }
    }
}
