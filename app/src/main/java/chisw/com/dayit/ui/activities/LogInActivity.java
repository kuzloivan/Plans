package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import chisw.com.dayit.R;
import chisw.com.dayit.utils.ValidData;


public class LogInActivity extends AuthorizationActivity {

    private Clicker mClicker;
    private InputFilter mInputFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        mInputFilter = initializeInputFilter();
        mLogin = (EditText) findViewById(R.id.la_userName_editText);
        mLogin.setSingleLine();
        mLogin.setFilters(new InputFilter[]{mInputFilter});
        mPassword = (EditText) findViewById(R.id.la_userPassword_editText);
        mPassword.setSingleLine();
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPassword.setFilters(new InputFilter[]{mInputFilter});
        if (ValidData.isTextValid(sharedHelper.getCurrentLogin())) {
            if (ValidData.isTextValid(sharedHelper.getUserPass())) {
                PlannerActivity.start(LogInActivity.this);
                LogInActivity.this.finish();
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, LogInActivity.class);
        a.startActivity(intent);
    }

    private void initView() {
        mClicker = new Clicker();
        findViewById(R.id.la_signUp_btn).setOnClickListener(mClicker);
        findViewById(R.id.la_logIn_btn).setOnClickListener(mClicker);
    }

    private InputFilter initializeInputFilter() {
        InputFilter inpF = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        return inpF;
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_log_in;
    }

    @Override
    protected void startSomeActivity() {
        finish();
    /*  PlannerActivity.start(LogInActivity.this);
        LogInActivity.this.finish();*/
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (prepareForClick()) {
                switch (v.getId()) {
                    case R.id.la_signUp_btn:
                        SignUpActivity.start(LogInActivity.this);
                        finish();
                        break;
                    case R.id.la_logIn_btn:
                        if (isValidFields()) {
                            showProgressDialog("Logging In", "Please, wait...");
                            netManager.loginUser(login, password, new CallbackLogIn());
                        }
                        break;
                }
            }
        }
    }

    private boolean isValidFields() {
        if (!ValidData.isCredentialsValid(login, getString(R.string.login_pttrn))) {
            showToast("Login must be at least 4 characters length and start with letter.(a-z,A-Z,0-9)");
            return false;
        }
        if (!ValidData.isCredentialsValid(password, getString(R.string.pass_pttrn))) {
            showToast("Password must be at least 6 characters length.(a-z,A-Z,0-9)");
            return false;
        }
        return true;
    }
}
