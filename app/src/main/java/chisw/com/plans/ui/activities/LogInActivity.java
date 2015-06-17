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
import chisw.com.plans.model.Plan;

public class LogInActivity extends ToolbarActivity {

    private EditText mLogin;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.btn_sign_up).setOnClickListener(clickerNet);
        findViewById(R.id.btn_log_in).setOnClickListener(clickerNet);
        findViewById(R.id.btn_plan).setOnClickListener(clickerNet);

        /* For testing */
        findViewById(R.id.btn_sph_tst).setOnClickListener(clickerNet);

        mLogin = (EditText) findViewById(R.id.net_user_login);
        mPassword = (EditText) findViewById(R.id.net_user_password);
        //auto-insert user credentials
        if (!TextUtils.isEmpty(sharedHelper.getDefaultLogin()))
        {
            mLogin.setText(sharedHelper.getDefaultLogin());
            if (!TextUtils.isEmpty(sharedHelper.getDefaultPass()))
            {
                mPassword.setText(sharedHelper.getDefaultPass());
                //move to main activity
                showProgressDialog("Loging In", "Please, wait...");
                netManager.loginUser(mLogin.getText().toString(), mPassword.getText().toString(), new CallbackLogIn());
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
            String login = mLogin.getText().toString();
            String password = mPassword.getText().toString();

            switch (v.getId()) {
                case R.id.btn_sign_up:
                    showProgressDialog("Signing Up", "Please, wait...");
                    netManager.registerUser(login, password, new CallbackSignUp());
                    break;

                case R.id.btn_log_in:
                    showProgressDialog("Loging In", "Please, wait...");
                    netManager.loginUser(login, password, new CallbackLogIn());
                    break;
                /* For testing!!! */
                case R.id.btn_sph_tst:
                    SplashActivity.start(LogInActivity.this);
                    break;
                case R.id.btn_plan:
                    /* Add Plan testing!!! */
                    /*Plan plan = new Plan();
                    plan.setTitle("test");
                    plan.setTimeStamp(111);
                    netManager.addPlan(plan, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                showToast("Plan was added");
                                return;
                            }
                            showToast(e.getMessage());
                        }
                    });*/
                    netManager.getPlan("8j4TGE9QPU", new FindCallback<ParseObject>() {
                        @Override
                        public void done(List list, ParseException e) {
                            if(e == null) {
                                showToast("I got it");
                                String testPlan = "";
                                for (int i = 0; i < 5; i++) {
                                    testPlan += list.get(i);
                                }
                                ((EditText)findViewById(R.id.net_user_login)).setText(testPlan);
                                return;
                            }
                            showToast(e.getMessage());
                        }

                    });
                    break;
            }
        }
    }

    public final class CallbackSignUp implements SignUpCallback {

        @Override
        public void done(ParseException e) {
            if (e != null) {
                showToast(e.getMessage());                
                hideProgressDialog();
                return;
            }
            sharedHelper.setDefaultLogin(mLogin.getText().toString());
            sharedHelper.setDefaultPass(mPassword.getText().toString());
            hideProgressDialog();
            showToast("Successful");
        }
    }

    public final class CallbackLogIn implements LogInCallback {

        @Override
        public void done(ParseUser parseUser, ParseException e) {
            if (e != null) {
                showToast(e.getMessage());
                hideProgressDialog();
                return;
            }
            ////under login sharedpreferences registration
            sharedHelper.setDefaultLogin(mLogin.getText().toString());
            sharedHelper.setDefaultPass(mPassword.getText().toString());
            ////
            hideProgressDialog();
            SplashActivity.start(LogInActivity.this);
            showToast("Login was successful");
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, LogInActivity.class);
        a.startActivity(intent);
    }
}
