package chisw.com.dayit.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import chisw.com.dayit.core.bridge.NetBridge;

import chisw.com.dayit.core.callback.CheckPhoneCallback;
import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnImageDownloadCompletedCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public class NetManager implements NetBridge {
    public static final String PLANS_TABLE_NAME = "Plans";
    public static final String USER_TABLE_NAME = "_User";
    public static final String PARSE_OBJECT_ID = "objectId";

    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String TIMESTAMP = "timeStamp";
    public static final String USER_ID = "userId";
    public static final String AUDIO_PATH = "audioPath";
    public static final String AUDIO_DURATION = "audioDuration";
    public static final String IMAGE_PATH = "imagePath";
    public static final String DAYS_TO_ALARM = "daysToAlarm";
    public static final String IS_REMOTE = "isRemote";

    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String IMAGE_FILE = "imageFile";

    @Override
    public void registerUser(String pName, String pPassword, String pPhone, SignUpCallback pSignUpCallback) {
        ParseUser user = new ParseUser();
        user.setUsername(pName);
        user.setPassword(pPassword);
        user.put(PHONE, pPhone);
        user.signUpInBackground(pSignUpCallback);
    }

    @Override
    public void loginUser(String pName, String pPassword, LogInCallback pLogInCallback) {
        ParseUser.logInInBackground(pName, pPassword, pLogInCallback);
    }

    @Override
    public void logoutUser(LogOutCallback pLogoutCallback) {
        ParseUser.logOutInBackground(pLogoutCallback);
    }

    @Override
    public void getUsersByNumbers(List<String> pPhoneNums, OnGetNumbersCallback pOnGetNumbersCallback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(PHONE, pPhoneNums);
        query.findInBackground(new CallbackGetNumbers(pOnGetNumbersCallback));
    }

    @Override
    public void getNumbersByUsers(List<String> pUsernames, OnGetNumbersCallback pOnGetNumbersCallback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(USERNAME, pUsernames);
        query.findInBackground(new CallbackGetNumbers(pOnGetNumbersCallback));
    }

    @Override
    public void addPlan(Plan pPlan, OnSaveCallback pOnSaveCallback) {
        ParseObject parsePlan = new ParseObject(PLANS_TABLE_NAME);
        parsePlan.put(TITLE, pPlan.getTitle());
        parsePlan.put(DETAILS, pPlan.getDetails());
        parsePlan.put(TIMESTAMP, pPlan.getTimeStamp());
        if (ValidData.isTextValid(pPlan.getImagePath())) {
            parsePlan.put(IMAGE_PATH, pPlan.getImagePath());
        }
        if (ValidData.isTextValid(plan.getImagePath())) {
            pPlan.put(IMAGE_PATH, plan.getImagePath());
            //if plan is remote image will be upload to parse
            if (plan.getIsRemote() == 1) {
                pPlan.put(IMAGE_FILE, uploadImage(plan.getImagePath()));
            }
        if (ValidData.isTextValid(pPlan.getAudioPath())) {
            parsePlan.put(AUDIO_PATH, pPlan.getAudioPath());
        }
        parsePlan.put(AUDIO_DURATION, pPlan.getAudioDuration());
        parsePlan.put(DAYS_TO_ALARM, pPlan.getDaysToAlarm());
        if(pPlan.getIsRemote() == 1) {
            parsePlan.put(IS_REMOTE, true);
        } else {
            parsePlan.put(IS_REMOTE, false);
        }
        parsePlan.put(USER_ID, ParseUser.getCurrentUser().getObjectId());
        parsePlan.saveInBackground(new CallbackAddPlan(parsePlan, pOnSaveCallback));
    }

    @Override
    public void getAllPlans(OnGetPlansCallback pOnGetPlansCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLANS_TABLE_NAME);
        query.whereEqualTo(USER_ID, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new CallbackGetPlans(pOnGetPlansCallback));
    }

    @Override
    public void getPlan(final String pParseId, GetCallback<ParseObject> pGetCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLANS_TABLE_NAME);
        query.whereEqualTo(PARSE_OBJECT_ID, pParseId);
        query.getInBackground(pParseId, pGetCallback);
    }

    @Override
    public void checkPhone(String pPhone, CheckPhoneCallback pCheckPhoneCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_TABLE_NAME);
        query.whereEqualTo(PHONE, pPhone);
        query.findInBackground(new CallBackPhone(pCheckPhoneCallback));
    }

    @Override
    public void editPlan(String pParseId, GetCallback<ParseObject> callbackEditPlan) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLANS_TABLE_NAME);
        query.whereEqualTo(PARSE_OBJECT_ID, pParseId);
        query.getInBackground(pParseId, callbackEditPlan);
    }

    @Override
    public void editUser(String pParseId, GetCallback<ParseUser> callBackEditUser){
        ParseQuery<ParseUser> query = ParseQuery.getQuery(USER_TABLE_NAME);
        query.whereEqualTo(PARSE_OBJECT_ID, pParseId);
        query.getInBackground(pParseId, callBackEditUser);
    }

    @Override
    public void deletePlan(String parseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PLANS_TABLE_NAME);
        query.getInBackground(parseId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground();
                }
            }
        });
    }

        @Override
        public ParseFile uploadImage(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile(image);
            file.saveInBackground();
            return file;
        }

        @Override
        public String downloadImage(String pTaskTitle, long pTimeStamp, String pParseID, final OnImageDownloadCompletedCallback object) {

            //String fname = "Image" + pTaskTitle + DataUtils.getDateStringFromTimeStamp(pTimeStamp) + ".jpg";
            String fname = "Image" + pTaskTitle + ".jpg";
            //String fname = "Image" + ".jpg";

            final File file = new File(SystemUtils.createDirectory(), fname);
            if (file.exists())
                file.delete();

            final ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
            query.whereEqualTo(IMAGE_FILE, pParseID);
            query.getInBackground(pParseID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject pParseObject, ParseException e) {
                    ParseFile fileObject = (ParseFile) pParseObject.get(IMAGE_FILE);
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] pBytes, ParseException e) {
                            if (e == null) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(pBytes, 0, pBytes.length);
                                saveImageToExternalStorage(bmp, file.getAbsolutePath(), object);
                            }
                        }
                    });
                }
            });
            return file.getAbsolutePath();
        }

        private void saveImageToExternalStorage(Bitmap finalBitmap, String
        path, OnImageDownloadCompletedCallback object) {
            OnImageDownloadCompletedCallback obj = object;
            try {
                File file = new File(path);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                obj.downloadSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
/*
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
*/

        }

        private fi

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

        private final class CallbackGetPlan implements GetCallback<ParseObject> {
            private final GetPlanCallback getPlanCallback;

            public CallbackGetPlan(GetPlanCallback callback) {
                this.getPlanCallback = callback;
            }

            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    getPlanCallback.getPlan(parseObject);
                }
            }
        }

    private final class CallbackGetPlans implements FindCallback<ParseObject> {
        private final ArrayList<Plan> plans;
        private final OnGetPlansCallback onGetPlansCallback;

        public CallbackGetPlans(OnGetPlansCallback onGetPlansCallback) {
            this.onGetPlansCallback = onGetPlansCallback;
            plans = new ArrayList<>();
        }

        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if (e == null) {
                for (ParseObject parsePlan : list) {
                    Plan p = new Plan();
                    p.setPlanFromParse(parsePlan);
                    plans.add(p);
                }
                onGetPlansCallback.getPlans(plans);
                return;
            }
            onGetPlansCallback.getPlans(null);
        }
    }
}
