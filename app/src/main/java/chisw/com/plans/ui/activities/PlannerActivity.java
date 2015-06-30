package chisw.com.plans.ui.activities;

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

import com.parse.LogOutCallback;
import com.parse.ParseException;

import java.util.Observable;
import java.util.Observer;

import chisw.com.plans.R;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;
import chisw.com.plans.others.FloatingActionButton;
import chisw.com.plans.ui.adapters.PlannerCursorAdapter;

public class PlannerActivity extends ToolbarActivity implements Observer {

    private ListView mLvPlanner;
    private PlannerCursorAdapter mAdapter;

    public static void start(Activity pActivity) {
        Intent intent = new Intent(pActivity, PlannerActivity.class);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
        startSynchronization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    private void initView() {
        Clicker clicker = new Clicker();
        ItemClicker itemClicker = new ItemClicker();
        mLvPlanner = (ListView) findViewById(R.id.pa_planner_listview);
        mAdapter = new PlannerCursorAdapter(this);
        mLvPlanner.setAdapter(mAdapter);
        mLvPlanner.setOnItemClickListener(itemClicker);
        registerForContextMenu(mLvPlanner);
        dbManager.addObserver(this);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.toolbar_background_color))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setId(R.id.fab);
        fabButton.setOnClickListener(clicker);
    }

    private void updateListView() {
        Cursor cursor = dbManager.getPlans();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
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
                    AlarmActivity.start(this, p.getLocalId());
                    break;

                case R.id.pa_context_delete:
                    deleteEntirely(cursor.getInt(idIndex));
                    break;
            }
        }
        return super.onContextItemSelected(pMenuItem);
    }

    public void deleteEntirely(int id) {
        alarmManager.cancelAlarm(id);
        if (!sharedHelper.getSynchronization()) {
            synchronization.wasDeleting((dbManager.getPlanById(id)).getLocalId());
        } else {
            netManager.deletePlan((dbManager.getPlanById(id)).getParseId());
        }
        dbManager.deletePlanById(id);
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
                while (cursor.moveToNext()) {
                    alarmManager.cancelAlarm(cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID)));
                }
                cursor.close();
                dbManager.clearPlans();
                dbManager.eraseMe(sharedHelper.getDefaultLogin());
                sharedHelper.clearData();
                LogInActivity.start(PlannerActivity.this);
                break;
        }
        return super.onOptionsItemSelected(pMenuItem);
    }

    private void startSynchronization() {
        synchronization.startSynchronization(getApplication().getApplicationContext());
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
                    AlarmActivity.start(PlannerActivity.this);
                    break;
            }
        }
    }
}

