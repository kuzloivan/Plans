package chisw.com.dayit.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import chisw.com.dayit.R;
import chisw.com.dayit.net.NetManager;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;

/**
 * Created by Kuzlo on 15.07.2015.
 */

public class UserActivity extends ToolbarActivity {

    private EditText mLoginET;
    private EditText mPhoneET;
    private EditText mPasswordET;
    private Button mSaveBT;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initViews();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_user;
    }

    private void initViews() {
        initBackButton();
        mLoginET = (EditText) findViewById(R.id.ua_login_et);
        mPhoneET = (EditText) findViewById(R.id.ua_phone_number_et);
        mPasswordET = (EditText) findViewById(R.id.ua_password_et);

        findViewById(R.id.ua_save_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoButtonsAlertDialog dialDelPlans = new TwoButtonsAlertDialog();
                dialDelPlans.setIAlertDialog(new SaveUserDialogClicker());
                dialDelPlans.setDialogTitle("Do you want to save a user?");
                dialDelPlans.setPositiveBtnText("Yes, I do");
                dialDelPlans.setNegativeBtnText("No, I don't");
                dialDelPlans.show(getFragmentManager(), getString(R.string.ua_save));
            }

        });

        mLoginET.setText(sharedHelper.getDefaultLogin());
        mPhoneET.setText(sharedHelper.getDefaultPhone());
        mPasswordET.setText(sharedHelper.getDefaultPass());

    }

    private final class SaveUserDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            netManager.editUser(ParseUser.getCurrentUser(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser pParseUser, ParseException e) {
                    pParseUser.setPassword(mPasswordET.getText().toString());
                    pParseUser.setUsername(mLoginET.getText().toString());
                    pParseUser.put(NetManager.PHONE, mPhoneET.getText().toString());
                    pParseUser.saveInBackground();
                }
            });

            sharedHelper.setDefaultLogin(mLoginET.getText().toString().toLowerCase());
            sharedHelper.setDefaultPhone(mPhoneET.getText().toString());
            sharedHelper.setDefaultPass(mPasswordET.getText().toString());

            showToast("User has been updated");
            finish();
        }
    }
}
