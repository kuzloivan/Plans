package chisw.com.plans.core.bridge;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import chisw.com.plans.model.Plan;

public interface NetBridge {

    /* Sing Up and Log In */
    void registerUser(String pName, String pPassword, SignUpCallback signUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback logInCallback);
    void logoutUser(String pName, String pPassword, LogOutCallback logOutCallback);

    /* Plan */
    void addPlan(Plan plan, SaveCallback saveCallback);
    void getAllPlans(FindCallback findCallback);
    void getPlan(String pId, FindCallback findCallback);

}
