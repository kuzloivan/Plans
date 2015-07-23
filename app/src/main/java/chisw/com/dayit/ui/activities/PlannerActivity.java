package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import chisw.com.dayit.R;
import chisw.com.dayit.core.callback.OnGetNumbersCallback;
import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.others.RestartManager;
import chisw.com.dayit.ui.adapters.PlannerCursorAdapter;
import chisw.com.dayit.ui.custom_element.FloatingActionButton;
import chisw.com.dayit.ui.dialogs.ContactListDialog;
import chisw.com.dayit.ui.dialogs.PasswordCheckDialog;
import chisw.com.dayit.ui.dialogs.TaskTypeDialog;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;


public class PlannerActivity extends ToolbarActivity implements Observer {

    private ListView mLvPlanner;
    private PlannerCursorAdapter mAdapter;
    private int mWantToDelete;
    private ArrayList<String> mContactArrayList;
    private ContactListDialog mContactListDialog;

    public static void start(Activity pActivity) {
        Intent intent = new Intent(pActivity, PlannerActivity.class);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedHelper.getDefaultLogin().isEmpty() && SystemUtils.checkNetworkStatus(getApplicationContext()) && sharedHelper.getSynchronization()) {
            startSynchronization();
        }
        updateListView();
    }

    private void initView() {
        Clicker clicker = new Clicker();
        ItemClicker itemClicker = new ItemClicker();
        mLvPlanner = (ListView) findViewById(R.id.pa_planner_listView);
        mAdapter = new PlannerCursorAdapter(this);
        mLvPlanner.setAdapter(mAdapter);
        mLvPlanner.setOnItemClickListener(itemClicker);
        registerForContextMenu(mLvPlanner);
        dbManager.addObserver(this);
        initFAB(clicker);
    }

    private void updateListView() {
        Cursor cursor = dbManager.getNotDeletedPlans();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    private void deleteAllItems() {
        Cursor cursor = dbManager.getNotDeletedPlans();
        cursor.moveToFirst();
        int id;
        do {
            id = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
            alarmManager.cancelAlarm(id);
            Plan plan = dbManager.getPlanById(id);
            plan.setIsSynchronized(0);
            plan.setIsDeleted(1);
            dbManager.editPlan(plan, id);
        } while (cursor.moveToNext());
        if (!sharedHelper.getDefaultLogin().isEmpty() && SystemUtils.checkNetworkStatus(getApplicationContext())) {
            startSynchronization();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenu.ContextMenuInfo pContextMenuInfo) {
        super.onCreateContextMenu(pContextMenu, pView, pContextMenuInfo);
        getMenuInflater().inflate(R.menu.context_menu_planner, pContextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem pMenuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) pMenuItem.getMenuInfo();
        Cursor cursor = mAdapter.getCursor();

        if (cursor.moveToPosition((int) (info.position))) {
            int idIndex = cursor.getColumnIndex(PlansEntity.LOCAL_ID);

            switch (pMenuItem.getItemId()) {
                case R.id.pa_context_edit:
                    Plan p = dbManager.getPlanById(cursor.getInt(idIndex));
                    if (p.getIsRemote() == 0) {
                        LocalTaskActivity.start(this, p.getLocalId());
                    }
                    if (p.getIsRemote() == 1) {
                        showToast("You can't edit remote plan");
                        // RemoteTaskActivity.start(this, p.getLocalId());
                    }
                    break;
                case R.id.pa_context_delete:
                    TwoButtonsAlertDialog dialDelPlan = new TwoButtonsAlertDialog();
                    dialDelPlan.setIAlertDialog(new DeletePlanDialogClicker());
                    dialDelPlan.setDialogTitle("Are you sure you want to delete this plan?");
                    dialDelPlan.setPositiveBtnText("Yes");
                    dialDelPlan.setNegativeBtnText("Cancel");
                    dialDelPlan.show(getFragmentManager(), getString(R.string.pa_delete_plan));
                    mWantToDelete = cursor.getInt(idIndex);
                    break;
                case R.id.pa_context_delete_all:
                    TwoButtonsAlertDialog dialDelPlans = new TwoButtonsAlertDialog();
                    dialDelPlans.setIAlertDialog(new DeleteAllPlansDialogClicker());
                    dialDelPlans.setDialogTitle("Are you sure you want to delete all plans?");
                    dialDelPlans.setPositiveBtnText("Yes");
                    dialDelPlans.setNegativeBtnText("Cancel");
                    dialDelPlans.show(getFragmentManager(), getString(R.string.pa_delete_all_plans));
                    break;
            }
        }
        return super.onContextItemSelected(pMenuItem);
    }

    public void deleteEntirely(int id) {
        alarmManager.cancelAlarm(id);
        Plan plan = dbManager.getPlanById(id);
        plan.setIsSynchronized(0);
        plan.setIsDeleted(1);
        dbManager.editPlan(plan, id);
        if (!sharedHelper.getDefaultLogin().isEmpty() && SystemUtils.checkNetworkStatus(getApplicationContext())) {
            startSynchronization();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        getMenuInflater().inflate(R.menu.menu_planner, pMenu);
        return super.onCreateOptionsMenu(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.pa_menu_sync:
                if (!sharedHelper.getDefaultLogin().isEmpty() && SystemUtils.checkNetworkStatus(getApplicationContext())) {
                    startSynchronization();
                } else if (sharedHelper.getDefaultLogin().isEmpty()) {
                    TwoButtonsAlertDialog dial = new TwoButtonsAlertDialog();
                    dial.setIAlertDialog(new AuthorizationDialogClicker());
                    dial.setDialogTitle("To synchronize the plans you should log in. Continue?");
                    dial.setPositiveBtnText("Yes");
                    dial.setNegativeBtnText("Cancel");
                    dial.show(getFragmentManager(), getString(R.string.pa_authorization));
                }
                break;

            case R.id.pa_menu_settings:
                SettingsActivity.start(PlannerActivity.this);
                break;
            case R.id.pa_menu_contacts:
                if (!ValidData.isTextValid(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass())) {
                    showToast("You aren't log in");
                }
                else {
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
                }
                break;
            case R.id.pa_menu_log_off:
                if (!ValidData.isTextValid(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass())) {
                    showToast("You aren't log in");
                } else {
                    showProgressDialog("Logging Off", "Please, wait...");
                    netManager.logoutUser(new CallbackLogOut());
                    Cursor cursor = dbManager.getPlans();
                    while (cursor.moveToNext()) {
                        alarmManager.cancelAlarm(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
                    }
                    cursor.close();
                }
                break;
            case R.id.pa_menu_user_activity:
                if (!ValidData.isTextValid(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass())) {
                    showToast("You aren't log in");
                } else {
                    PasswordCheckDialog passwordCheckDialog = new PasswordCheckDialog();
                    passwordCheckDialog.setListener(new DeletePlanDialogClicker());
                    passwordCheckDialog.show(getSupportFragmentManager(), "passCheck");
                }
                break;
        }
        return super.onOptionsItemSelected(pMenuItem);
    }

    private final class ContactDialog implements ContactListDialog.IContact {

        @Override
        public void getPhone(String pPhoneNumber) {
        }
        @Override
        public void onDismiss() {
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

    private void startSynchronization() {
        netManager.getAllPlans(new CallbackGetAllPlans());
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_planner;
    }

    @Override
    public void update(Observable pObservable, Object pData) {
        updateListView();
    }

    private final class CallbackLogOut implements LogOutCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                showToast(e.getMessage());
                hideProgressDialog();
                return;
            }
            dbManager.eraseMe(sharedHelper.getDefaultLogin());
            ParsePush.unsubscribeInBackground(sharedHelper.getDefaultLogin());
            sharedHelper.clearUserData();
            hideProgressDialog();
            showToast("Logged out");
        }
    }

    private final class ItemClicker implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(position);

            int planId = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
            Plan plan = dbManager.getPlanById(planId);

            ViewPlanActivity.start(PlannerActivity.this, plan.getLocalId());
        }
    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab:
                    if (!sharedHelper.getDefaultLogin().isEmpty() && SystemUtils.checkNetworkStatus(PlannerActivity.this)) {
                        new TaskTypeDialog().show(getFragmentManager(), "TaskType");
                    } else {
                        LocalTaskActivity.start(PlannerActivity.this);
                    }
                    break;
            }
        }
    }

    private final class CallbackEditPlan implements GetCallback<ParseObject> {
        private final Plan plan;

        public CallbackEditPlan(Plan plan) {
            this.plan = plan;
        }

        @Override
        public void done(ParseObject parseObject, ParseException e) {
            if (e == null) {
                parseObject.put("title", plan.getTitle());
                parseObject.put("timeStamp", plan.getTimeStamp());
                if (ValidData.isTextValid(plan.getAudioPath())) {
                    parseObject.put("audioPath", plan.getAudioPath());
                }
                if (ValidData.isTextValid(plan.getImagePath())) {
                    parseObject.put("imagePath", plan.getImagePath());
                }
                parseObject.put("audioDuration", plan.getAudioDuration());
                parseObject.put("details", plan.getDetails());
                parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
                parseObject.saveInBackground();
            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                dbManager.deletePlanById(plan.getLocalId());
            } else {
                showToast(e.getMessage());
            }
        }
    }

    private final class CallbackSavePlan implements OnSaveCallback {

        private Plan mPlan;

        public CallbackSavePlan(Plan pPlan) {
            mPlan = pPlan;
        }

        @Override
        public void getId(String id, long updatedAtParseTime) {
            if (ValidData.isTextValid(id)) {
                mPlan.setParseId(id);
                mPlan.setUpdatedAtParseTime(updatedAtParseTime);
                int planId = mPlan.getLocalId();
                mPlan.setIsSynchronized(1);
                dbManager.editPlan(mPlan, planId);
            }
        }
    }

    private final class CallbackGetAllPlans implements OnGetPlansCallback {

        @Override
        public void getPlans(ArrayList<Plan> pListPlans) {
            if (pListPlans != null) {
                for (Plan parsePlan : pListPlans) {
                    if(dbManager.getPlanByParseId(parsePlan.getParseId()) == null) {
                        dbManager.saveNewPlan(parsePlan);
                    }
                }
                if (!pListPlans.isEmpty()) {
                    RestartManager restartManager = new RestartManager(getApplication().getApplicationContext());
                    restartManager.reload();
                }
            }

            Cursor cursor = dbManager.getPlans();
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    final Plan localPlan = dbManager.getPlanById(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));

                    if (localPlan.getIsSynchronized() == 0) {
                        if (ValidData.isTextValid(localPlan.getParseId())) {
                            if (localPlan.getIsDeleted() == 1) {
                                netManager.deletePlan(localPlan.getParseId());
                                dbManager.deletePlanById(localPlan.getLocalId());
                            } else {
                                netManager.editPlan(localPlan.getParseId(), new CallbackEditPlan(localPlan));
                                localPlan.setIsSynchronized(1);
                                dbManager.editPlan(localPlan, localPlan.getLocalId());
                            }
                        } else {
                            if (localPlan.getIsDeleted() == 1) {
                                dbManager.deletePlanById(localPlan.getLocalId());
                            } else {
                                netManager.addPlan(localPlan, new CallbackSavePlan(localPlan));
                                localPlan.setIsSynchronized(1);
                            }
                        }
                    } else {
                        netManager.getPlan(localPlan.getParseId(), new CallbackGetPlan(localPlan));
                    }
                    cursor.moveToNext();
                }
            }
        }
    }

    private final class CallbackGetPlan implements GetCallback<ParseObject> {

        private Plan localPlan;

        public CallbackGetPlan(Plan pLocalPlan) {
            localPlan = pLocalPlan;
        }

        @Override
        public void done(ParseObject parsePlan, ParseException e) {
            if(e == null) {
                if(parsePlan.getUpdatedAt().getTime() > localPlan.getUpdatedAtParseTime()) {
                    Plan plan = new Plan();
                    plan.setPlanFromParse(parsePlan);
                    plan.setIsSynchronized(1);
                    dbManager.editPlan(plan, localPlan.getLocalId());
                }
            } else if(e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                dbManager.deletePlanById(localPlan.getLocalId());
            }
        }
    }

    private void initFAB(Clicker clicker){
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.color_floating_action_button))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 15, 15)
                .create();
        fabButton.setId(R.id.fab);
        fabButton.setOnClickListener(clicker);
    }

    private final class DeletePlanDialogClicker implements TwoButtonsAlertDialog.IAlertDialog, PasswordCheckDialog.IPasswordCheckDialog{

        @Override
        public void onAcceptClick() {
            deleteEntirely(mWantToDelete);
        }

        @Override
        public void onDialogPositiveClick(String pPass){
            if(pPass.equals(sharedHelper.getDefaultPass())) {
                Intent intent = new Intent(PlannerActivity.this, EditUserActivity.class);
                startActivity(intent);
            }
            else
                showToast("Password is incorrect");
        }

        @Override
        public void onDialogNegativeClick(){

        }
    }

    private final class DeleteAllPlansDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            deleteAllItems();
            showToast("All plans have been deleted");
        }
    }

    private final class AuthorizationDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            LogInActivity.start(PlannerActivity.this);
        }
    }
}

