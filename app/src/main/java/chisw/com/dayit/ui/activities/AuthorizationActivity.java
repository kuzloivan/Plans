package chisw.com.dayit.ui.activities;

import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import chisw.com.dayit.R;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

/**
 * Created by vdbo on 02.07.15.
 */
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
                showToast(error); //test
                hideProgressDialog();
                return;
            }
            sharedHelper.setDefaultLogin(mLogin.getText().toString().toLowerCase());
            sharedHelper.setDefaultPass(mPassword.getText().toString());
            hideProgressDialog();
            showToast("Login was successful");
            startSomeActivity();
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

//    protected boolean isValidFields() {
//        if(!ValidData.isCredentialsValid(login, getString(R.string.login_pttrn))){
//            showToast("Login must be at least 4 characters length.(a-z,A-Z,0-9)");
//            return false;
//        }
//        if(!ValidData.isCredentialsValid(password, getString(R.string.pass_pttrn))){
//            showToast("Password must be at least 6 characters length.(a-z,A-Z,0-9)");
//            return false;
//        }
//        return true;
//    }

    protected abstract void startSomeActivity();
}
