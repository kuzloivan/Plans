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

        mLogin = (EditText) findViewById(R.id.net_user_login);
        mLogin.setSingleLine();
        mInputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++){
                    if(!Character.isLetterOrDigit(source.charAt(i))){
                        return "";
                    }
                }
                return null;
            }
        };
        mLogin.setFilters(new InputFilter[] { mInputFilter });
        mPassword = (EditText) findViewById(R.id.net_user_password);
        mPassword.setSingleLine();
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
        mClicker = new Clicker();
        findViewById(R.id.btn_to_sign_up).setOnClickListener(mClicker);
        findViewById(R.id.btn_log_in).setOnClickListener(mClicker);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_log_in;
    }

    @Override
    protected void startSomeActivity() {
        PlannerActivity.start(LogInActivity.this);
        LogInActivity.this.finish();
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(prepareForClick()) {
                switch (v.getId()) {
                    case R.id.btn_to_sign_up:
                        SignUpActivity.start(LogInActivity.this);
                        break;
                    case R.id.btn_log_in:
                        if (isValidFields()) {
                            showProgressDialog("Logging In", "Please, wait...");
                            netManager.loginUser(login, password, new CallbackLogIn());
                        }
                        break;
                }
            }
        }
    }
}
