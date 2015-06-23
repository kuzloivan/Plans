package chisw.com.plans.core.bridge;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.SignUpCallback;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.activities.AlarmActivity;

public interface NetBridge {

    /* Sing Up and Log In */
    void registerUser(String pName, String pPassword, SignUpCallback signUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback logInCallback);
    void logoutUser(String pName, String pPassword, LogOutCallback logOutCallback);

    /* Plan */
    void addPlan(Plan plan, OnSaveCallback callback);
    void getAllPlans(FindCallback findCallback);
    void getPlan(String pId, FindCallback findCallback);
    void editPlan(Plan plan, AlarmActivity.CallbackEditPlan callbackEditPlan);
    void deletePlan(String pId, GetCallback getCallback);

}
