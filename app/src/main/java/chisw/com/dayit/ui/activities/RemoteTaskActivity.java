package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.ui.dialogs.ContactListDialog;
import chisw.com.dayit.utils.DataUtils;

public class RemoteTaskActivity extends TaskActivity {
    private static final String BUNDLE_ID_KEY = "chisw.com.plans.ui.activities.remoteTask_activity.id";
    private static final String BUNDLE_KEY = "chisw.com.plans.ui.activities.remoteTask_activity.bundle";

    private boolean mIsContactDialogExist;
    private TextView mTvPhone;
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
        dateFillIn();
    }

    @Override
    protected void initViews() {
        super.initViews();

        mRClicker = new RClicker();
        findViewById(R.id.rta_get_contact_list_btn).setOnClickListener(mRClicker);
        mTvDate.setOnClickListener(mRClicker);
        mTvTime.setOnClickListener(mRClicker);
        mSwitchRepeating.setOnClickListener(mRClicker);
        mIvImage.setOnClickListener(mRClicker);

        mTvPhone = (TextView) findViewById(R.id.rta_phone_tv);
    }

    @Override
    protected void startAlarm() {
        sendRemotePlan();
        writePlanToDB(DataUtils.getCalendar());
        super.startAlarm();
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
            mTvPhone.setText(pPhoneNumber);
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
                // String name = cursor.getString(0);

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

    private void sendRemotePlan() {
        String[] splited = mTvPhone.getText().toString().split("\\s+");
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        try {
            data.put("alert", mEtTitle.getText().toString());
            data.put("title", mTvSetDetails.getText().toString());
            data.put("time", Long.toString(DataUtils.getCalendar().getTimeInMillis()));
        } catch (JSONException ex) {
            return;
        }
        push.setData(data);
        push.setChannel(splited[0]);
        push.sendInBackground(new CallbackRemotePlan());
    }

    private void writePlanToDB(Calendar calendar) {
        if (!super.checkFields()) {
            return;
        }
        Plan p = new Plan();
        p.setIsRemote(1);
        super.writePlanToDB(calendar, p);
    }

    private final class RClicker extends TaskActivity.Clicker {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.aa_save_alarm:
                    startAlarm();
                    break;
                case R.id.rta_get_contact_list_btn:
                    if (mIsContactDialogExist) {
                        return;
                    }
                    mIsContactDialogExist = true;
                    ArrayList<String> contactsArrayList = initializeList();
                    mContactArrayList = new ArrayList<String>();
                    netManager.getUsersByNumbers(contactsArrayList, new OnGetNumbersCallback() {
                        @Override
                        public void getNumbers(Map<String, String> numbers) {
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
                        }
                    });
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
                showToast("Your remote plan was sending successfully!");
                return;
            }
            showToast(e.getMessage());
        }
    }
}