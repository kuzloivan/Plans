package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.LogOutCallback;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;
import chisw.com.plans.others.FloatingActionButton;
import chisw.com.plans.ui.adapters.PlannerCursorAdapter;

public class PlannerActivity extends ToolbarActivity implements Observer {

    ListView lvPlanner;
    PlannerCursorAdapter plannerCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Clicker clicker = new Clicker();

        ItemClicker itemClicker = new ItemClicker();
        ItemLongClicker itemLongClicker = new ItemLongClicker();

        lvPlanner = (ListView) findViewById(R.id.pa_planner_listview);
        plannerCursorAdapter = new PlannerCursorAdapter(this);
        lvPlanner.setAdapter(plannerCursorAdapter);

        lvPlanner.setOnItemClickListener(itemClicker);
        lvPlanner.setOnItemLongClickListener(itemLongClicker);

        registerForContextMenu(lvPlanner);
        dbManager.addObserver(this);

        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.toolbar_background_color))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setId(R.id.fab);
        //fabButton.showFloatingActionButton();
        fabButton.setOnClickListener(clicker);

        startSynchronization();
    }

    private void updateListView() {
        Cursor cursor = dbManager.getPlans();
        plannerCursorAdapter.swapCursor(cursor);
        plannerCursorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_planner, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = plannerCursorAdapter.getCursor();

        if (cursor.moveToPosition((int) (info.position))) {

            int idIndex = cursor.getColumnIndex(PlansEntity.LOCAL_ID);

            switch (item.getItemId()) {
                case R.id.pa_context_edit:

                    Plan p = dbManager.getPlanById(cursor.getInt(idIndex));
                    AlarmActivity.start(this, p.getLocalId());

                    break;

                case R.id.pa_context_delete:

                    deleteEntirely(cursor, idIndex);

                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Deprecated
    public void deleteEntirely(Cursor cursor, int idIndex){
        alarmManager.cancelAlarm(cursor);

        if(!sharedHelper.getSynchronization()) {
            synchronization.wasDeleting(dbManager.getPlanById(cursor.getInt(idIndex)).getLocalId());
        }
        else {
            netManager.deletePlan((dbManager.getPlanById(cursor.getInt(idIndex))).getParseId());
        }

        dbManager.deletePlanById(cursor.getInt(idIndex));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.pa_menu_sync:
                startSynchronization();
                break;

            case R.id.pa_menu_settings:
                SettingsActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_log_off:
                //Log off!
                showProgressDialog("Loging Off", "Please, wait...");
                netManager.logoutUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogOut());
                Cursor cursor = dbManager.getPlans();

                while(cursor.moveToNext()) {
                    alarmManager.cancelAlarm(cursor);
                }
                cursor.close();
                dbManager.clearPlans();
                dbManager.eraseMe(sharedHelper.getDefaultLogin());
                sharedHelper.clearData();
                LogInActivity.start(PlannerActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSynchronization() {
        synchronization.startSynchronization(getApplication().getApplicationContext());
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PlannerActivity.class);
        activity.startActivity(intent);
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

    public final class ItemLongClicker implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // todo: add some code?

            return false;
        }
    }

    public final class ItemClicker implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = plannerCursorAdapter.getCursor();
            cursor.moveToPosition(position);

            int planId = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
            Plan plan = dbManager.getPlanById(planId);

            ViewPlanActivity.start(PlannerActivity.this, plan.getLocalId());
//
//            Cursor cursor = plannerCursorAdapter.getCursor();
//
//            cursor.moveToPosition(position);
//
//            int planId = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
//            Plan plan = dbManager.selectPlanById(planId);
//
//            showToast(plan.getTitle());

        }
    }

    @Override
    public void update(Observable observable, Object data) {
        updateListView();
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab:
                    AlarmActivity.start(PlannerActivity.this);
                    break;
            }
        }
    }
}

