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
        setContentView(R.layout.activity_net_management);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.btn_sign_up).setOnClickListener(clickerNet);
        findViewById(R.id.btn_log_in).setOnClickListener(clickerNet);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_net_management;
    }

    public final class ClickerNet implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_sign_up:
                    netManager.registerUser(((EditText)findViewById(R.id.net_user_login)).getText().toString(),
                            ((EditText)findViewById(R.id.net_user_password)).getText().toString(),
                            new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(NetManagementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NetManagementActivity.this, "successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case R.id.btn_log_in:
                    netManager.loginUser(((EditText)findViewById(R.id.net_user_login)).getText().toString(),
                            ((EditText)findViewById(R.id.net_user_password)).getText().toString(),
                            new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if(e != null) {
                                Toast.makeText(NetManagementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NetManagementActivity.this, "login was successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagementActivity.class);
        a.startActivity(intent);
    }


}
