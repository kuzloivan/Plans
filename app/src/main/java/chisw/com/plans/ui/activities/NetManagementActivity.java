package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.TooManyListenersException;

import chisw.com.plans.R;

public class NetManagementActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.btn_sign_up).setOnClickListener(clickerNet);
        findViewById(R.id.btn_log_in).setOnClickListener(clickerNet);

        /* For testing */
        findViewById(R.id.btn_sph_tst).setOnClickListener(clickerNet);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_net_management;
    }

    public final class ClickerNet implements View.OnClickListener {

        String login;
        String password;

        @Override
        public void onClick(View v) {
            login = ((EditText)findViewById(R.id.net_user_login)).getText().toString();
            password = ((EditText)findViewById(R.id.net_user_password)).getText().toString();

            switch(v.getId()) {
                case R.id.btn_sign_up:
                    netManager.registerUser(login, password, new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                showToast(e.getMessage());
                            } else {
                                showToast("Successful");
                            }
                        }
                    });
                    break;
                case R.id.btn_log_in:
                    netManager.loginUser(login, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if(e != null) {
                                showToast(e.getMessage());
                            } else {
                                showToast("Login was successful");
                                SplashActivity.start(NetManagementActivity.this);
                            }
                        }
                    });
                    break;
                /*
                 * For testing!!!
                 */
                case R.id.btn_sph_tst:
                    SplashActivity.start(NetManagementActivity.this);
                    break;
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagementActivity.class);
        a.startActivity(intent);
    }


}
