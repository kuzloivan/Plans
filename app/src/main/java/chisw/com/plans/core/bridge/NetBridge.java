package chisw.com.plans.core.bridge;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public interface NetBridge {

    void registerUser(String pName, String pPassword, SignUpCallback signUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback logInCallback);

}
