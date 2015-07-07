package chisw.com.dayit.core.bridge;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseObject;
import com.parse.SignUpCallback;

import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.model.Plan;


public interface NetBridge {

    /* Sing Up and Log In */
    void registerUser(String pName, String pPassword, SignUpCallback signUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback logInCallback);
    void logoutUser(String pName, String pPassword, LogOutCallback logOutCallback);

    /* Plan */
    void addPlan(Plan plan, OnSaveCallback callback);
    void getAllPlans(OnGetPlansCallback callback);
    Plan getPlan(String pId);
    void editPlan(Plan plan, GetCallback<ParseObject> callbackEditPlan);
    void deletePlan(String pId);

}
