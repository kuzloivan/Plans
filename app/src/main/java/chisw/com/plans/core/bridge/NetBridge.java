package chisw.com.plans.core.bridge;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.SignUpCallback;

import java.util.List;

import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.activities.AlarmActivity;

public interface NetBridge {

    /* Sing Up and Log In */
    void registerUser(String pName, String pPassword, SignUpCallback signUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback logInCallback);
    void logoutUser(String pName, String pPassword, LogOutCallback logOutCallback);

    /* Plan */
    void addPlan(Plan plan, OnSaveCallback callback);
    List<Plan> getAllPlans(FindCallback findCallback);
    Plan getPlan(String pId);
    void editPlan(Plan plan, AlarmActivity.CallbackEditPlan callbackEditPlan);
    void deletePlan(String pId);

}
