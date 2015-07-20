package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import chisw.com.dayit.R;
import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.ui.dialogs.ContactListDialog;
import chisw.com.dayit.utils.ValidData;

public class RemoteTaskActivity extends TaskActivity {
    private static final String BUNDLE_ID_KEY = "chisw.com.DayIt.ui.activities.remoteTask_activity.id";
    private static final String BUNDLE_KEY = "chisw.com.DayIt.ui.activities.remoteTask_activity.bundle";
    private boolean mIsContactDialogExist;
    private EditText mTextContact;
    private ContactListDialog mContactListDialog;
    private ArrayList<String> mContactArrayList;
    private RClicker mRClicker;

    public static void start(Activity a) {
        Intent i = new Intent(a, RemoteTaskActivity.class);
        a.startActivity(i);
    }

    public static void start(Activity a, int id) {
        Intent i = new Intent(a, RemoteTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, id);
        i.putExtra(BUNDLE_KEY, bundle);
        a.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        if (getIntent().hasExtra(BUNDLE_KEY)) {   // this if is to edit remote plan // delete if it cause any problems
            mIsEdit = true;
            mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        }
        dateFillIn();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mRClicker = new RClicker();
        findViewById(R.id.rta_contactList_btn).setOnClickListener(mRClicker);
        mTvDate.setOnClickListener(mRClicker);
        mTvTime.setOnClickListener(mRClicker);
        mSwitchRepeating.setOnClickListener(mRClicker);
        mIvImage.setOnClickListener(mRClicker);
        mTextContact = (EditText) findViewById(R.id.rta_contactInfo_editText);
    }

    @Override
    protected void startAlarm() {
        if (ValidData.isTextValid(mTextContact.getText().toString()) && checkFields()) {
            try {
                writePlanToDB(mMyLovelyCalendar);
            } catch (Exception ex) {
                return;
            }
            super.startAlarm();
        } else if (!ValidData.isTextValid(mTextContact.getText().toString())) {
            showToast("Please, choose a contact person");
        }
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_remote_task;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private final class ContactDialog implements ContactListDialog.IContact {

        @Override
        public void getPhone(String pPhoneNumber) {
            mTextContact.setText(pPhoneNumber);
            mIsContactDialogExist = false;
        }

        @Override
        public void onDismiss() {
            mIsContactDialogExist = false;
        }
    }

    private ArrayList<String> initializeList() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = dbManager.getAllContacts(this);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String phone = cursor.getString(1);

                if (phone.charAt(0) == '+' && phone.length() > 9) {
                    phone = phone.replaceAll(" ", "");
                    list.add(phone);
                } else if (phone.charAt(0) != '+' && phone.length() > 9) {
                    phone = "+38" + phone.replaceAll(" ", "");
                    list.add(phone);
                }
            }
        }
        return list;
    }

    private void sendRemotePlan(String id) throws Exception {
        long interval = (mMyLovelyCalendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        if (interval < 0) {
            showToast("Time is wrong");
            throw new Exception();
        }
        String[] splited = mTextContact.getText().toString().split("\\s+");
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        try {
            data.put("alert", mEtTitle.getText().toString());
            data.put("title", mTvSetDetails.getText().toString());
            data.put("time", Long.toString(mMyLovelyCalendar.getTimeInMillis()));
            data.put("from", sharedHelper.getDefaultLogin());
        } catch (JSONException ex) {
            return;
        }
        push.setData(data);
        push.setExpirationTimeInterval(interval);
        push.setChannel(splited[0]);
        push.sendInBackground(new CallbackRemotePlan());
    }

    private void writePlanToDB(Calendar calendar) throws Exception {
        if (!super.checkFields()) {
            throw new Exception();
        }
        Plan p = new Plan();
        p.setIsRemote(1);
        super.writePlanToDB(calendar, p);
        if(p.getIsRemote() == 1)
        {
            netManager.addPlan(p, new OnSaveCallback() {
                @Override
                public void getId(String id, long updatedAtParseTime) {
                    try {
                        sendRemotePlan(id);
                    } catch (Exception ex) {
                        return;
                    }
                }
            });
        }
    }

    private final class RClicker extends TaskActivity.Clicker {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rta_contactList_btn:
                    if (mIsContactDialogExist) {
                        return;
                    }
                    mIsContactDialogExist = true;
                    ArrayList<String> contactsArrayList = initializeList();
                    mContactArrayList = new ArrayList<String>();
                    showProgressDialog("Contacts", "Getting numbers...");
                    netManager.getUsersByNumbers(contactsArrayList, new OnGetNumbersCallback() {
                        @Override
                        public void getNumbers(Map<String, String> numbers) {
                            hideProgressDialog();
                            if (numbers.size() != 0) {
                                mContactArrayList.clear();
                                for (Map.Entry<String, String> nums : numbers.entrySet()) {
                                    String contactInfo = nums.getValue() + " " + nums.getKey();
                                    mContactArrayList.add(contactInfo);
                                }
                                mContactListDialog = new ContactListDialog();
                                mContactListDialog.setIContact(new ContactDialog());
                                Bundle contactsBundle = new Bundle();
                                contactsBundle.putStringArrayList("contactsArrayList", mContactArrayList);
                                mContactListDialog.setArguments(contactsBundle);
                                mContactListDialog.show(getSupportFragmentManager(), "ContactListDialog");
                                return;
                            }
                            showToast("Contact list is empty");
                        }
                    });
                    mIsContactDialogExist = false;
                    break;
                default:
                    super.onClick(v);
            }
        }
    }

    private final class CallbackRemotePlan implements SendCallback {

        @Override
        public void done(ParseException e) {
            if (e == null) {
                showToast("Your remote plan was sent successfully!");
                return;
            }
            showToast(e.getMessage());
        }
    }
}
