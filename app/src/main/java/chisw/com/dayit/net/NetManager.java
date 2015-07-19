package chisw.com.dayit.net;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import chisw.com.dayit.core.bridge.NetBridge;

import chisw.com.dayit.core.callback.CheckPhoneCallback;
import chisw.com.dayit.core.callback.GetPlanCallback;
import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.utils.ValidData;


public class NetManager implements NetBridge {
    public static final String TABLE_NAME = "Plans";
    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String TIMESTAMP = "timeStamp";
    public static final String USER_ID = "userId";
    public static final String AUDIO_PATH = "audioPath";
    public static final String AUDIO_DURATION = "audioDuration";
    public static final String IMAGE_PATH = "imagePath";
    public static final String DAYS_TO_ALARM = "daysToAlarm";
    public static final String PLAN_ID = "objectId";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";

    @Override
    public void registerUser(String name, String password, String pPhone, SignUpCallback signUpCallback) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.put(PHONE, pPhone);
        user.signUpInBackground(signUpCallback);
    }

    @Override
    public void loginUser(String name, String password, LogInCallback logInCallback) {
        ParseUser.logInInBackground(name, password, logInCallback);
    }

    @Override
    public void logoutUser(LogOutCallback logoutCallback) {
        ParseUser.logOutInBackground(logoutCallback);
    }

    @Override
    public void getUsersByNumbers(List<String> phoneNums, OnGetNumbersCallback onGetNumbersCallback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(PHONE, phoneNums);
        query.findInBackground(new CallbackGetNumbers(onGetNumbersCallback));
    }

    @Override
    public void getNumbersByUsers(List<String> userNames, OnGetNumbersCallback onGetNumbersCallback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(USERNAME, userNames);
        query.findInBackground(new CallbackGetNumbers(onGetNumbersCallback));
    }
    @Override
    public void addPlan(Plan plan, OnSaveCallback callback) {
        ParseObject pPlan = new ParseObject(TABLE_NAME);
        pPlan.put(TITLE, plan.getTitle());
        pPlan.put(TIMESTAMP, plan.getTimeStamp());
        if (ValidData.isTextValid(plan.getAudioPath())) {
            pPlan.put(AUDIO_PATH, plan.getAudioPath());
        }
        if (ValidData.isTextValid(plan.getImagePath())) {
            pPlan.put(IMAGE_PATH, plan.getImagePath());
        }
        pPlan.put(DETAILS, plan.getDetails());
        pPlan.put(USER_ID, ParseUser.getCurrentUser().getObjectId());
        pPlan.put(AUDIO_DURATION, plan.getAudioDuration());
        pPlan.put(DAYS_TO_ALARM, plan.getDaysToAlarm());
        pPlan.saveInBackground(new CallbackAddPlan(pPlan, callback));
    }

    @Override
    public void getAllPlans(OnGetPlansCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo(USER_ID, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new CallbackGetPlans(callback));
    }

    @Override
    public void getPlan(final String parseId, GetCallback<ParseObject> pGetCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo(PLAN_ID, parseId);
        query.getInBackground(parseId, pGetCallback);
    }

    @Override
    public void checkPhone(String phone, CheckPhoneCallback checkPhoneCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo(PHONE, phone);
        query.findInBackground(new CallBackPhone(checkPhoneCallback));
    }

    @Override
    public void editPlan(Plan plan, GetCallback<ParseObject> callbackEditPlan) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo(PLAN_ID, plan.getParseId());
        query.getInBackground(plan.getParseId(), callbackEditPlan);
    }

    @Override
    public void editUser(ParseUser pParseUser, GetCallback<ParseUser> callBackEditUser){
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId" ,pParseUser.getObjectId());
        query.getInBackground(pParseUser.getObjectId(), callBackEditUser);
    }

    @Override
    public void deletePlan(String parseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.getInBackground(parseId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground();
                }
            }
        });
    }

    public final class CallbackGetNumbers implements FindCallback<ParseUser> {

        private HashMap<String, String> numbers;
        private OnGetNumbersCallback onGetNumbersCallback;

        public CallbackGetNumbers(OnGetNumbersCallback onGetNumbersCallback) {
            this.onGetNumbersCallback = onGetNumbersCallback;
            numbers = new HashMap<>();
        }

        @Override
        public void done(List<ParseUser> list, ParseException e) {
            if (e == null) {
                for (ParseUser obj : list) {
                    numbers.put(obj.getString(PHONE), obj.getString(USERNAME));
                }
                onGetNumbersCallback.getNumbers(numbers);
                return;
            }
            onGetNumbersCallback.getNumbers(null);
        }
    }

    private final class CallBackPhone implements FindCallback<ParseObject>{
        private CheckPhoneCallback checkPhoneCallback;

        public CallBackPhone(CheckPhoneCallback checkPhoneCallback) {
            this.checkPhoneCallback = checkPhoneCallback;
        }

        @Override
        public void done(List<ParseObject> list, ParseException e) {
           if(e == null && !list.isEmpty())
           {
               checkPhoneCallback.isNumberTaken(true);
               return;
           }
            checkPhoneCallback.isNumberTaken(false);
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
                onSaveCallback.getId(parsePlan.getObjectId(), parsePlan.getUpdatedAt().getTime());
                return;
            }
            onSaveCallback.getId(null, -1);
        }

    }

    public final class CallbackGetPlans implements FindCallback<ParseObject> {
        private final ArrayList<Plan> plans;
        private final OnGetPlansCallback onGetPlansCallback;

        public CallbackGetPlans(OnGetPlansCallback onGetPlansCallback) {
            this.onGetPlansCallback = onGetPlansCallback;
            plans = new ArrayList<>();
        }

        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if (e == null) {
                for (ParseObject obj : list) {
                    Plan p = new Plan();
                    p.setAudioPath(obj.getString(PlansEntity.AUDIO_PATH));
                    p.setImagePath(obj.getString(PlansEntity.IMAGE_PATH));
                    p.setDetails(obj.getString(PlansEntity.DETAILS));
                    p.setTitle(obj.getString(PlansEntity.TITLE));
                    p.setTimeStamp(obj.getLong(PlansEntity.TIMESTAMP));
                    p.setAudioDuration(obj.getInt(PlansEntity.AUDIO_DURATION));
                    p.setDaysToAlarm(obj.getString(PlansEntity.DAYS_TO_ALARM));
                    p.setParseId(obj.getObjectId());
                    p.setUpdatedAtParseTime(obj.getUpdatedAt().getTime());
                    plans.add(p);
                }
                onGetPlansCallback.getPlans(plans);
                return;
            }
            onGetPlansCallback.getPlans(null);
        }
    }
}
