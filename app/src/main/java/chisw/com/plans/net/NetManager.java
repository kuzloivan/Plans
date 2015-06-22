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
    public void registerUser(String name, String password, SignUpCallback signUpCallback) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground(signUpCallback);
    }

    @Override
    public void loginUser(String name, String password, LogInCallback logInCallback) {
        ParseUser.logInInBackground(name, password, logInCallback);
    }

    @Override
    public void logoutUser(String name, String pPassword, LogOutCallback logoutCallback) {
        ParseUser.logOutInBackground(logoutCallback);
    }

    @Override
    public void addPlan(Plan plan, OnSaveCallback callback) {
        ParseObject parsePlan = new ParseObject("Plans");
        parsePlan.put("name", plan.getTitle());
        parsePlan.put("timeStamp", plan.getTimeStamp());
        parsePlan.put("details", plan.getDetails());
        parsePlan.saveInBackground(new CallbackAddPlan(parsePlan, callback));
    }

    @Override
    public void getAllPlans(FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.findInBackground(findCallback);
    }

    @Override
    public void getPlan(String parseId, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", parseId);
        query.findInBackground(findCallback);
    }

    @Override
    public void editPlan(String parseId, GetCallback getCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(parseId, getCallback);
    }

    @Override
    public void deletePlan(String parseId, GetCallback getCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(parseId, getCallback);
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
