package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.SignUpCallback;

import chisw.com.dayit.R;
import chisw.com.dayit.utils.ValidData;

public class SignUpActivity extends AuthorizationActivity {

    private Clicker mClicker;
    private EditText mPhone;
    private EditText mPasswordConfirm;
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
        mLogin = (EditText) findViewById(R.id.sua_userName_editText);
        mPassword = (EditText) findViewById(R.id.sua_userPassword_editText);
        mPhone = (EditText) findViewById(R.id.sua_userPhone_editText);
        mPasswordConfirm = (EditText) findViewById(R.id.sua_userPasswordConfirm_editText);
        findViewById(R.id.sua_signUp_btn).setOnClickListener(mClicker);
        findViewById(R.id.sua_back_btn).setOnClickListener(mClicker);
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
                    case R.id.sua_signUp_btn:
                        if (isValidFields()) {
                            showProgressDialog("Signing Up", "Please, wait...");
                            netManager.registerUser(login, password, phone, new CallbackSignUp());
                        }
                        break;
                    case R.id.sua_back_btn:
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
            showToast("SignUp was successful");
            hideProgressDialog();
        }
    }

    private boolean isValidFields() {
        if(!ValidData.isPhoneNumberValid(phone)){
            showToast("Phone number is not valid!");
            return false;
        }
        if (!ValidData.isCredentialsValid(login, getString(R.string.login_pttrn))) {
            showToast("Login must be at least 4 characters length.(a-z,A-Z,0-9)");
            return false;
        }
        if (!ValidData.isCredentialsValid(password, getString(R.string.pass_pttrn))) {
            showToast("Password must be at least 6 characters length.(a-z,A-Z,0-9)");
            return false;
        }
        if (!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())) {
            showToast("Passwords don't match!");
            return false;
        }
        return true;
    }
}
