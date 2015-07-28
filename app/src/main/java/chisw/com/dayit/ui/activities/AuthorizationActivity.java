package chisw.com.dayit.ui.activities;

import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Map;

import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.utils.SystemUtils;

public abstract class AuthorizationActivity extends ToolbarActivity {

    protected EditText mLogin;
    protected EditText mPassword;
    protected String login;
    protected String password;

    @Override
    protected int contentViewResId() {
        return 0;
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
                showToast(e.getMessage()); //test
                hideProgressDialog();
                return;
            }
            sharedHelper.setCurrentLogin(mLogin.getText().toString().toLowerCase());
            if(!sharedHelper.getCurrentLogin().equals(sharedHelper.getLastLogin())) {
                dbManager.deletePlans();
            }
            sharedHelper.setUserPass(mPassword.getText().toString());
            sharedHelper.setLastLogin(sharedHelper.getCurrentLogin());
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(mLogin.getText().toString().toLowerCase());

            netManager.getNumbersByUsers(arrayList, new OnGetNumbersCallback() {
                @Override
                public void getNumbers(Map<String, String> phones) {
                    for (Map.Entry<String, String> nums : phones.entrySet()) {
                        sharedHelper.setUserPhone(nums.getKey());
                    }
                }
            });

            ParsePush.subscribeInBackground(sharedHelper.getCurrentLogin(), new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        showToast("Bind to channel was successful");
                        hideProgressDialog();
                        showToast("Login was successful");
                        startSomeActivity();
                        return;
                    }
                    showToast(e.getMessage());
                }
            });
        }
    }

    protected boolean prepareForClick() {
        if(!SystemUtils.checkNetworkStatus(getApplicationContext()))
        {
            showToast("No internet connection");
            return false;
        }
        login = mLogin.getText().toString().toLowerCase();
        password = mPassword.getText().toString();
        return true;
    }

    protected abstract void startSomeActivity();
}
