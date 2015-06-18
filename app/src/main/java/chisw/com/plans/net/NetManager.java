package chisw.com.plans.net;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
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

    /**
     * User sign up on Parse
     * @param pName user's name
     * @param pPassword user's password
     * @param signUpCallback callback function
     */
    @Override
    public void registerUser(String pName, String pPassword, SignUpCallback signUpCallback) {
        ParseUser user = new ParseUser();
        user.setUsername(pName);
        user.setPassword(pPassword);
        user.signUpInBackground(signUpCallback);
    }

    /**
     * User log in on Parse
     * @param pName user's name
     * @param pPassword user's password
     * @param logInCallback callback function
     */
    @Override
    public void loginUser(String pName, String pPassword, LogInCallback logInCallback) {
        ParseUser.logInInBackground(pName, pPassword, logInCallback);
    }

    /**
     * User log out on Parse
     * @param pName user's name
     * @param pPassword user's password
     * @param logoutCallback callback function
     */
    @Override
    public void logoutUser(String pName, String pPassword, LogOutCallback logoutCallback) {
        ParseUser.logOutInBackground(logoutCallback);
    }

    /**
     * Add plan on Parse
     * @param plan plan object
     * @param saveCallback callback function
     */
    @Override
    public void addPlan(Plan plan, SaveCallback saveCallback) {
        ParseObject pPlan = new ParseObject("Plans");
        pPlan.put("name", plan.getTitle());
        pPlan.put("timeStamp", plan.getTimeStamp());
        pPlan.saveInBackground(saveCallback);
    }

    /**
     * Get plan from Parse
     * @param findCallback callback function
     */
    @Override
    public void getAllPlans(FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.findInBackground(findCallback);
    }

    /**
     * Get plan from Parse
     * @param pId plan id
     * @param findCallback callback function
     */
    @Override
    public void getPlan(String pId, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", pId);
        query.findInBackground(findCallback);
    }
}
