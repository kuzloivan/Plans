package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import chisw.com.plans.R;
import chisw.com.plans.utils.SystemUtils;
import chisw.com.plans.utils.ValidData;

public class LogInActivity extends ToolbarActivity {

    private EditText mLogin;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.btn_sign_up).setOnClickListener(clickerNet);
        findViewById(R.id.btn_log_in).setOnClickListener(clickerNet);

        /* For testing */
        mLogin = (EditText) findViewById(R.id.net_user_login);
        mPassword = (EditText) findViewById(R.id.net_user_password);

        /* Auto-insert user credentials */
        if (ValidData.isTextValid(sharedHelper.getDefaultLogin()))
        {
            mLogin.setText(sharedHelper.getDefaultLogin());
            if (ValidData.isTextValid(sharedHelper.getDefaultPass())) {
                mPassword.setText(sharedHelper.getDefaultPass());
                /* Move to main activity */
                PlannerActivity.start(LogInActivity.this);
                LogInActivity.this.finish();
            }
        }
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_log_in;
    }

    public final class ClickerNet implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            /* Checking of network connection */
            if(!SystemUtils.checkNetworkStatus(getApplicationContext()))
            {
                showToast("No internet connection");
                return;
            }
            String login = mLogin.getText().toString();
            String password = mPassword.getText().toString();
            /* Checking of network connection */
            if(!ValidData.isTextValid(login,password))
            {
                showToast("Process failed: empty string");
                return;
            }
            /* Buttons listener */
            switch (v.getId()) {
                case R.id.btn_sign_up:
                    showProgressDialog("Signing Up", "Please, wait...");
                    netManager.registerUser(login, password, new CallbackSignUp());
                    break;
                case R.id.btn_log_in:
                    showProgressDialog("Logging In", "Please, wait...");
                    netManager.loginUser(login, password, new CallbackLogIn());
                    break;
            }
        }
    }

    public final class CallbackSignUp implements SignUpCallback {

        String error = "";

        @Override
        public void done(ParseException e) {
            if (e != null) {
                /* Is username already exist */
                switch(e.getCode()) {
                    case ParseException.USERNAME_TAKEN:
                        error = "Username is already taken";
                        break;
                }
                showToast(error); //test
                hideProgressDialog();
                return;
            }
            /* Save user credentials and then Log In */
            sharedHelper.setDefaultLogin(mLogin.getText().toString());
            sharedHelper.setDefaultPass(mPassword.getText().toString());

            netManager.loginUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogIn());

            hideProgressDialog();
            showToast("Successful");
        }
    }

    public final class CallbackLogIn implements LogInCallback {

        @Override
        public void done(ParseUser parseUser, ParseException e) {
            if (e != null) {
                showToast("error"); //test
                hideProgressDialog();
                return;
            }
            /* Under login sharedpreferences registration */
            sharedHelper.setDefaultLogin(mLogin.getText().toString());
            sharedHelper.setDefaultPass(mPassword.getText().toString());

            showToast("LogIn was successful");
            hideProgressDialog();

            PlannerActivity.start(LogInActivity.this);
            LogInActivity.this.finish();
        }
    }

    /* For future release of shared plans/tasks */
    public final class CallbackAddPlan implements SaveCallback {

        @Override
        public void done(ParseException e) {
            if(e == null) {
                showToast("Plan was added");
                return;
            }
            showToast(e.getMessage());
        }
    }

    /* For future release of shared plans/tasks */
    public final class CallbackGetPlan implements FindCallback<ParseObject> {

        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if(e == null) {
                showToast("I've got it");
                return;
            }
            showToast(e.getMessage());
        }
    }

    /* For future release of shared plans/tasks */
    public final class CallbackGetPlans implements FindCallback<ParseObject> {

        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if(e == null) {
                showToast("I've got it");
                return;
            }
            showToast(e.getMessage());
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, LogInActivity.class);
        a.startActivity(intent);
    }
}
