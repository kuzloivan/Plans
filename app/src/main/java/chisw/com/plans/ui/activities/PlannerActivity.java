package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.db.Mapper;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.adapters.PlannerCursorAdapter;
import chisw.com.plans.utils.DataUtils;

public class PlannerActivity extends ToolbarActivity {

    ListView lvPlanner;
    PlannerCursorAdapter plannerCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Clicker clicker = new Clicker();

        ItemClicker itemClicker = new ItemClicker();
        ItemLongClicker itemLongClicker = new ItemLongClicker();

        lvPlanner = (ListView)findViewById(R.id.pa_planner_listview);
        plannerCursorAdapter = new PlannerCursorAdapter(this);
        lvPlanner.setAdapter(plannerCursorAdapter);

        lvPlanner.setOnItemClickListener(itemClicker);
        lvPlanner.setOnItemLongClickListener(itemLongClicker);

        registerForContextMenu(lvPlanner);

        // ------------ For test only ------------ //
//        Plan p = new Plan();
//        p.setTitle("Make it!");
//        p.setTimeStamp(Calendar.getInstance().getTimeInMillis());
//        dbManager.saveNewPlan(p);
        // ------------ For test only ------------ //

    }

    private void updateListView(){
        Cursor cursor = dbManager.getPlans();

        if(cursor.moveToFirst()){
            plannerCursorAdapter.swapCursor(cursor);
        }
        else {
            showToast("Your plans list is empty");
        }
    }

    @Override
    protected void onResume() {
        updateListView();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu_planner, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.pa_context_edit:

                showToast("Edit selected plan");
                // todo: implement edit activity.

                break;

            case R.id.pa_context_delete:

                // // FIXME: 18.06.2015 incorrect deleting

                Cursor cursor = plannerCursorAdapter.getCursor();
                boolean result = cursor.moveToPosition((int)(info.id - 1));

                int idIndex = cursor.getColumnIndex(PlansEntity.LOCAL_ID);

                if(dbManager.deletePlanById(cursor.getInt(idIndex)) == 1){
                    showToast("Plan deleted");
                } else{
                    showToast("Unexpected error occurred");
                }

                updateListView();

                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.pa_menu_add_reminder:
                AlarmActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_settings:
                SettingsActivity.start(PlannerActivity.this);
                break;

            case R.id.pa_menu_log_off:
                //Log off!
                showProgressDialog("Loging Off", "Please, wait...");
                netManager.logoutUser(sharedHelper.getDefaultLogin(), sharedHelper.getDefaultPass(), new CallbackLogOut());
                dbManager.clearPlans();
                dbManager.eraseMe(sharedHelper.getDefaultLogin());
                sharedHelper.clearData();
                LogInActivity.start(PlannerActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public static void start(Activity activity){
        Intent intent = new Intent(activity, PlannerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_planner;
    }

    public final class CallbackLogOut implements LogOutCallback
    {
        @Override
        public void done(ParseException e)
        {
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

    public final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    }

    public final class ItemLongClicker implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // todo: add some code?

            return false;
        }
    }

    public final class ItemClicker implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // todo: go to edit activity

        }
    }
}
