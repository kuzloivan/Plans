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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import chisw.com.dayit.R;
import chisw.com.dayit.core.callback.OnGetPlansCallback;
import chisw.com.dayit.core.callback.OnSaveCallback;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.others.RestartManager;
import chisw.com.dayit.ui.adapters.PlannerCursorAdapter;
import chisw.com.dayit.ui.custom_element.FloatingActionButton;
import chisw.com.dayit.ui.dialogs.DeleteDialog;
import chisw.com.dayit.ui.dialogs.TaskTypeDialog;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public class PlannerActivity extends ToolbarActivity implements Observer {

    private ListView mLvPlanner;
    private PlannerCursorAdapter mAdapter;
    private int mWantToDelete;

    public static void start(Activity pActivity) {
        Intent intent = new Intent(pActivity, PlannerActivity.class);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
        if (SystemUtils.checkNetworkStatus(getApplicationContext()) && sharedHelper.getSynchronization()) {
            startSynchronization();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.color_floating_action_button))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 15, 15)
                .create();
        fabButton.setId(R.id.fab);
        fabButton.setOnClickListener(clicker);
    }

    private void updateListView() {
        Cursor cursor = dbManager.getNotDeletedPlans();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    private void deleteAllItems(){
        Cursor cursor = dbManager.getNotDeletedPlans();
        cursor.moveToFirst();
        do{
            deleteEntirely(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
        }
        while(cursor.moveToNext());
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
                    if(p.getIsRemote() == 0)
                    {
                        LocalTaskActivity.start(this, p.getLocalId());
                    }
                    if(p.getIsRemote() == 1)
                    {
                        RemoteTaskActivity.start(this, p.getLocalId());
                    }
                    break;
                case R.id.pa_context_delete:
                    DeleteDialog dial = new DeleteDialog();
                    dial.setIDelete(new DeleteDialogClicker());
                    dial.show(getFragmentManager(), "Delete dialog");
                    mWantToDelete = cursor.getInt(idIndex);
                    break;
                case R.id.pa_context_delete_all:
                    DeleteDialog dialDeleteAll = new DeleteDialog();
                    dialDeleteAll.setIDelete(new DeleteDialogClicker());
                    dialDeleteAll.show(getFragmentManager(), "Delete dialog");
                    mWantToDelete = -1;
                    showToast("All plans have been deleted");
                    break;
            }
        }
        return super.onContextItemSelected(pMenuItem);
    }

    public void deleteEntirely(int id) {
        alarmManager.cancelAlarm(id);
        Plan plan = dbManager.getPlanById(id);
        if (!sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(getApplicationContext())) {
            plan.setIsSynchronized(0);
            plan.setIsDeleted(1);
            dbManager.editPlan(plan, id);
        } else {
            netManager.deletePlan(plan.getParseId());
            dbManager.deletePlanById(id);
            plan.setIsSynchronized(1);
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
                if (SystemUtils.checkNetworkStatus(getApplicationContext())) {
                    startSynchronization();
                }
                break;

            case R.id.pa_menu_settings:
                SettingsActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_log_off:
                //Log off!
                showProgressDialog("Logging Off", "Please, wait...");
                netManager.logoutUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogOut());
                Cursor cursor = dbManager.getPlans();
                while (cursor.moveToNext()) {
                    alarmManager.cancelAlarm(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
                }
                cursor.close();
                dbManager.clearPlans();
                dbManager.eraseMe(sharedHelper.getDefaultLogin());
                sharedHelper.clearData();
                LogInActivity.start(PlannerActivity.this);
                break;
            case R.id.pa_user_activity:
                SettingsActivity.start(UserActivity.this);
                break;
        }
        return super.onOptionsItemSelected(pMenuItem);
    }

    private void startSynchronization() {
        Cursor cursor = dbManager.getPlans();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final Plan plan = dbManager.getPlanById(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));

                if (plan.getIsSynchronized() == 0) {
                    if (ValidData.isTextValid(plan.getParseId())) {
                        if (plan.getIsDeleted() == 1) {
                            netManager.deletePlan(plan.getParseId());
                            dbManager.deletePlanById(plan.getLocalId());
                        } else {
                            netManager.editPlan(plan, new CallbackEditPlan(plan));
                            plan.setIsSynchronized(1);
                            dbManager.editPlan(plan, plan.getLocalId());
                        }
                    } else {
                        if (plan.getIsDeleted() == 1) {
                            dbManager.deletePlanById(plan.getLocalId());
                        } else {
                            netManager.addPlan(plan, new OnSaveCallback() {
                                @Override
                                public void getId(String id) {
                                    if(ValidData.isTextValid(id)) {
                                        plan.setParseId(id);
                                        int planId = plan.getLocalId();
                                        plan.setIsSynchronized(1);
                                        dbManager.editPlan(plan, planId);
                                    }
                                }
                            });
                        }
                    }

                }
                cursor.moveToNext();
            }
        } else {
            final ArrayList<Plan> plans = new ArrayList<>();
            netManager.getAllPlans(new OnGetPlansCallback() {
                @Override
                public void getPlans(ArrayList<Plan> lPlans) {
                    if(lPlans != null) {
                        for (Plan plan : lPlans) {
                            plans.add(plan);
                            dbManager.saveNewPlan(plan);
                        }
                        if (!lPlans.isEmpty()) {
                            RestartManager restartManager = new RestartManager(getApplication().getApplicationContext());
                            restartManager.reload();
                        }
                    }
                }
            });
        }
        cursor.close();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_planner;
    }

    public final class CallbackLogOut implements LogOutCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                showToast(e.getMessage());
                hideProgressDialog();
                return;
            }
            dbManager.clearPlans();
            dbManager.eraseMe(sharedHelper.getDefaultLogin());
            sharedHelper.clearData();

            PlannerActivity.this.finish();

            hideProgressDialog();
            showToast("Logged out");
        }
    }

    public final class ItemClicker implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(position);

            int planId = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
            Plan plan = dbManager.getPlanById(planId);

            ViewPlanActivity.start(PlannerActivity.this, plan.getLocalId());
        }
    }

    @Override
    public void update(Observable pObservable, Object pData) {
        updateListView();
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab:
                    if (SystemUtils.checkNetworkStatus(PlannerActivity.this)) {
                        new TaskTypeDialog().show(getFragmentManager(), "TaskType");
                    } else {
                        LocalTaskActivity.start(PlannerActivity.this);
                    }
                    break;
            }
        }
    }

    public final class CallbackEditPlan implements GetCallback<ParseObject> {
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
            } else {
                showToast(e.getMessage());
            }
        }
    }

    private final class DeleteDialogClicker implements DeleteDialog.IDelete {

        @Override
        public void onDeleteOkClick() {
            if (mWantToDelete != -1){
                deleteEntirely(mWantToDelete);
            }
            else {
                deleteAllItems();
            }
        }
    }

}

