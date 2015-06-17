package chisw.com.plans.net;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import chisw.com.plans.core.bridge.NetBridge;

/**
 * Created by vdbo on 16.06.15.
 */
public class NetManager implements NetBridge {

    @Override
    public void registerUser(String pName, String pPassword, SignUpCallback signUpCallback) {
        ParseUser user = new ParseUser();
        user.setUsername(pName);
        user.setPassword(pPassword);
        user.signUpInBackground(signUpCallback);
    }

    @Override
    public void loginUser(String pName, String pPassword, LogInCallback logInCallback) {
        ParseUser.logInInBackground(pName, pPassword, logInCallback);
    }
}
