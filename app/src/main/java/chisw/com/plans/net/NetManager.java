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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chisw.com.plans.core.bridge.GetPlanCallback;
import chisw.com.plans.core.bridge.NetBridge;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.db.Mapper;
import chisw.com.plans.db.entity.PlansEntity;
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
    public List<Plan> getAllPlans(FindCallback findCallback) {
        List<Plan> plans = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject obj : list){
                    Plan p = new Plan();
                    p.setAudioPath(obj.getString(PlansEntity.AUDIO_PATH));
                    p.setLocalId(obj.getInt(PlansEntity.LOCAL_ID));
                    p.setDetails(obj.getString(PlansEntity.DETAILS));
                    p.setTitle(obj.getString(PlansEntity.TITLE));
                    p.setTimeStamp(obj.getLong(PlansEntity.TIMESTAMP));
                    plans.add(p);
                }
            }
        });
        return plans;
    }

    @Override
    public Plan getPlan(String parseId) {
        final Plan p = new Plan();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", parseId);
        query.getInBackground(parseId, new CallbackGetPlan(new GetPlanCallback() {
            @Override
            public void getPlan(ParseObject parseObject) {
                p.setAudioPath(parseObject.getString(PlansEntity.AUDIO_PATH));
                p.setLocalId(parseObject.getInt(PlansEntity.LOCAL_ID));
                p.setDetails(parseObject.getString(PlansEntity.DETAILS));
                p.setTitle(parseObject.getString(PlansEntity.TITLE));
                p.setTimeStamp(parseObject.getLong(PlansEntity.TIMESTAMP));
            }
        }));
        return p;
    }

    @Override
    public void editPlan(Plan plan, AlarmActivity.CallbackEditPlan callbackEditPlan) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.whereEqualTo("objectId", plan.getParseId());
        query.getInBackground(plan.getParseId(), callbackEditPlan);
    }

    @Override
    public void deletePlan(String parseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Plans");
        query.getInBackground(parseId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.deleteInBackground();
            }
        });
    }

    public final class CallbackGetPlan implements GetCallback<ParseObject>{
        private final GetPlanCallback getPlanCallback;

        public CallbackGetPlan( GetPlanCallback callback){
            this.getPlanCallback=callback;
        }

        @Override
        public void done(ParseObject parseObject, ParseException e) {
            if (e==null){
                getPlanCallback.getPlan(parseObject);
            }
        }
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
                return;
            }
            e.getMessage();
        }

    }

}
