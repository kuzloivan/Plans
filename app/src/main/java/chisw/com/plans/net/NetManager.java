package chisw.com.plans.net;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import chisw.com.plans.core.bridge.NetBridge;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.activities.BaseActivity;
import chisw.com.plans.ui.activities.LogInActivity;

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

    @Override
    public void logoutUser(String pName, String pPassword, LogOutCallback logoutCallback) {
        ParseUser.logOutInBackground(logoutCallback);
    }

    @Override
    public void addPlan(Plan plan, OnSaveCallback callback) {
        String parseId;
        ParseObject pPlan = new ParseObject("Plans");
        pPlan.put("name", plan.getTitle());
        pPlan.put("timeStamp", plan.getTimeStamp());
        pPlan.saveInBackground(new CallbackAddPlan(pPlan, callback));
    }

    @Override
    public void getAllPlans(FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.findInBackground(findCallback);
    }

    @Override
    public void getPlan(String pId, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", pId);
        query.findInBackground(findCallback);
    }

    @Override
    public void editPlan(String pId, GetCallback getCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(pId, getCallback);
    }

    @Override
    public void deletePlan(String pId, GetCallback getCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(pId, getCallback);
    }

    public final class CallbackAddPlan implements SaveCallback {

        private final ParseObject parsePlan;
        private OnSaveCallback onSaveCallback;

        public CallbackAddPlan(ParseObject parsePlan, OnSaveCallback onSaveCallback) {
            this.onSaveCallback = onSaveCallback;
            this.parsePlan = parsePlan;
        }

        @Override
        public void done(ParseException e) {
           if(e == null) {
               onSaveCallback.getId(parsePlan.getObjectId());
               return;
           }
        }
    }
}
