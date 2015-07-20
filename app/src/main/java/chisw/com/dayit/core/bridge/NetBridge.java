package chisw.com.dayit.core.bridge;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import chisw.com.dayit.core.callback.CheckPhoneCallback;
import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnImageDownloadCompletedCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.model.Plan;


public interface NetBridge {

    /* Sing Up and Log In */
    void registerUser(String pName, String pPassword, String pPhone, SignUpCallback pSignUpCallback);
    void loginUser(String pName, String pPassword, LogInCallback pLogInCallback);
    void logoutUser(LogOutCallback pLogOutCallback);
    void getUsersByNumbers(List<String> pPhoneNums, OnGetNumbersCallback pOnGetNumbersCallback);
    void checkPhone(String phone, CheckPhoneCallback checkPhoneCallback);
    /* Plan */
    void addPlan(Plan plan, OnSaveCallback pOnSaveCallback);
    void getAllPlans(OnGetPlansCallback pOnGetCallback);
    void getPlan(String pId, GetCallback<ParseObject> pGetCallback);
    void editPlan(String pParseId, GetCallback<ParseObject> pGetCallback);
    void deletePlan(String pId);
    void getNumbersByUsers(List<String> pUsernames, OnGetNumbersCallback pOnGetNumbersCallback);
    void editUser(String pParseId, GetCallback<ParseUser> pGetCallback);
    ParseFile uploadImage(String path);
    String downloadImage(String taskTitle, long timeStamp, String pImagePath, OnImageDownloadCompletedCallback object);

}
