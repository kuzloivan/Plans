package chisw.com.plans.net;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import chisw.com.plans.core.bridge.NetBridge;
import chisw.com.plans.model.Plan;

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

    public void logoutUser(String pName, String pPassword, LogOutCallback logoutCallback) {
        ParseUser.logOutInBackground(logoutCallback);

    }


    @Override
    public void addPlan(Plan plan, SaveCallback saveCallback) {
        ParseObject pPlan = new ParseObject("Plans");
        pPlan.add("id", plan.getParseId());
        pPlan.add("name", plan.getTitle());
        pPlan.add("timeStamp", plan.getTimeStamp());
        pPlan.saveInBackground(saveCallback);
    }

    @Override
    public void getAllPlans(FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.findInBackground(findCallback);
    }

    @Override
    public void getPlan(Integer pId, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", pId).findInBackground(findCallback);
    }
}
