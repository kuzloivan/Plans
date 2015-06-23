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

import java.util.List;

import chisw.com.plans.core.bridge.NetBridge;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.BaseActivity;
import chisw.com.plans.ui.activities.LogInActivity;
import chisw.com.plans.utils.ValidData;


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
        ParseObject pPlan = new ParseObject("Plans");
        pPlan.put("name", plan.getTitle());
        pPlan.put("timeStamp", plan.getTimeStamp());
        if(ValidData.isTextValid(plan.getAudioPath())) {
            pPlan.put("audioPath", plan.getAudioPath());
        }
        pPlan.put("details", plan.getDetails());
        pPlan.put("userId", ParseUser.getCurrentUser().getObjectId());
        pPlan.saveInBackground(new CallbackAddPlan(pPlan, callback));
    }

    @Override
    public void getAllPlans(FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.findInBackground(findCallback);
    }

    @Override
    public void getPlan(String parseId, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(findCallback);

    }

    @Override
    public void editPlan(Plan plan, AlarmActivity.CallbackEditPlan callbackEditPlan) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", plan.getParseId());
        query.getInBackground(plan.getParseId(), callbackEditPlan);
    }

    @Override
    public void deletePlan(String parseId, GetCallback getCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(parseId, getCallback);
    }

    public final class CallbackAddPlan implements SaveCallback {

        private final ParseObject parsePlan;
        private final OnSaveCallback onSaveCallback;

        public CallbackAddPlan(ParseObject parsePlan, OnSaveCallback onSaveCallback) {
            this.parsePlan = parsePlan;
            this.onSaveCallback = onSaveCallback;
        }

        @Override
        public void done(ParseException e) {
            if (e == null) {
                onSaveCallback.getId(parsePlan.getObjectId());
            }
        }

    }

}
