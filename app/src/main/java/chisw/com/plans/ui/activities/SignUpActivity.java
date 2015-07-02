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
import chisw.com.plans.utils.ValidData;

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
            String login = mLogin.getText().toString().toLowerCase();
            String password = mPassword.getText().toString();
            switch (v.getId()) {
                case R.id.btn_sign_up:
                    if(!ValidData.isCredentialsValid(login, getString(R.string.login_pttrn))){
                        showToast("Login must be at least 4 characters length.(a-z,A-Z,0-9)");
                        return;
                    }
                    if(!ValidData.isCredentialsValid(password, getString(R.string.pass_pttrn))){
                        showToast("Password must be at least 6 characters length.(a-z,A-Z,0-9)");
                        return;
                    }
                    showProgressDialog("Signing Up", "Please, wait...");
                    netManager.registerUser(login, password, new CallbackSignUp());
                    break;
                case R.id.btn_back_to_log_in:
                    LogInActivity.start(SignUpActivity.this);
                    break;
            }
        }
    }

    public final class CallbackSignUp implements SignUpCallback {
        String error = "Error";
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
            sharedHelper.setDefaultLogin(mLogin.getText().toString().toLowerCase());
            sharedHelper.setDefaultPass(mPassword.getText().toString());
            netManager.loginUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogIn());
            hideProgressDialog();
            showToast("SignUp was successful");
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
            PlannerActivity.start(SignUpActivity.this);
            SignUpActivity.this.finish();
        }
    }
}
