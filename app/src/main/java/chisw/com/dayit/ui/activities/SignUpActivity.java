package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import chisw.com.dayit.R;

/**
 * Created by Oksana on 30.06.2015.
 */
public class SignUpActivity extends AuthorizationActivity {

    private Clicker mClicker;
    private EditText mPhone;
    private String phone;

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

    @Override
    protected void startSomeActivity() {
        PlannerActivity.start(SignUpActivity.this);
        SignUpActivity.this.finish();
    }

    private void initView() {
        mClicker = new Clicker();
        mLogin = (EditText) findViewById(R.id.new_user_login);
        mPassword = (EditText) findViewById(R.id.new_user_password);
        mPhone = (EditText) findViewById(R.id.new_user_phone);
        findViewById(R.id.btn_sign_up).setOnClickListener(mClicker);
        findViewById(R.id.btn_back_to_log_in).setOnClickListener(mClicker);
    }

    @Override
    protected boolean prepareForClick() {
        phone = mPhone.getText().toString();
        return super.prepareForClick();
    }

    public final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (prepareForClick()) {
                switch (v.getId()) {
                    case R.id.btn_sign_up:
                        if (isValidFields()) {
                            showProgressDialog("Signing Up", "Please, wait...");
                            netManager.registerUser(login, password, phone, new CallbackSignUp());
                        }
                        break;
                    case R.id.btn_back_to_log_in:
                        onBackPressed();
                        break;
                }
            }
        }
    }

    public final class CallbackSignUp implements SignUpCallback {
        String error = "Error";
        @Override
        public void done(ParseException e) {
            if (e != null) {
                /* Is username already exist */
                switch (e.getCode()) {
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

            //Testing parse's pushes
            ParsePush.subscribeInBackground(sharedHelper.getDefaultLogin(), new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        showToast("Bind to channel was successful");
                        return;
                    }
                    showToast(e.getMessage());
                }
            });

            showToast("SignUp was successful");
            hideProgressDialog();
        }
    }
}
