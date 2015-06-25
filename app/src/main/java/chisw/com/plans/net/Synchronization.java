package chisw.com.plans.net;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.bridge.OnGetPlansCallback;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.core.bridge.SyncBridge;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.model.Plan;
import chisw.com.plans.others.RestartManager;
import chisw.com.plans.ui.activities.AlarmActivity;
import chisw.com.plans.ui.activities.LogInActivity;
import chisw.com.plans.utils.ValidData;

/**
 * Created by vdboo_000 on 23.06.2015.
 */
public class Synchronization implements SyncBridge {
    private static final String ADD_PLAN = "0";
    private static final String EDIT_PLAN = "1";
    private static final String DELETE_LOCAL_PLAN = "2";

    public  Map<Integer, String> historyOfChanges;
    private DBManager dbManager;
    private NetManager netManager;
    private boolean isSyncStarted;

    public Synchronization(DBManager dbManager, NetManager netManager) {
        this.historyOfChanges = new HashMap<>();
        this.dbManager = dbManager;
        this.netManager = netManager;
        isSyncStarted = false;
    }

    public void wasAdding(int planId) {
        historyOfChanges.put(planId, ADD_PLAN);
    }

    public void wasEditing(int planId) {
        historyOfChanges.put(planId, EDIT_PLAN);
    }

    public void wasDeleting(int planId) {
        if(dbManager.getPlanById(planId).getParseId() != null) {
            historyOfChanges.put(planId, dbManager.getPlanById(planId).getParseId());
            return;
        }
        historyOfChanges.put(planId, DELETE_LOCAL_PLAN);
    }

    public void startSynchronization(final Context ctxt) {
        if(!historyOfChanges.isEmpty()) {
            if (!isSyncStarted) {
                isSyncStarted = true;
            } else {
                return;
            }
            for (Integer key : historyOfChanges.keySet()) {
                final Plan plan = dbManager.getPlanById(key);
                switch (historyOfChanges.get(key)) {
                    case ADD_PLAN:
                        netManager.addPlan(plan, new OnSaveCallback() {
                            @Override
                            public void getId(String id) {
                                plan.setParseId(id);
                                int planId = plan.getLocalId();
                                dbManager.editPlan(plan, planId);
                                isSyncStarted = false;
                            }
                        });
                        break;
                    case EDIT_PLAN:
                        if (plan.getParseId() == null) {
                            netManager.addPlan(plan, new OnSaveCallback() {
                                @Override
                                public void getId(String id) {
                                    plan.setParseId(id);
                                    int planId = plan.getLocalId();
                                    dbManager.editPlan(plan, planId);
                                    isSyncStarted = false;
                                }
                            });
                            return;
                        }
                        netManager.editPlan(plan, new CallbackEditPlan(plan));
                        break;
                    case DELETE_LOCAL_PLAN:
                        isSyncStarted = false;
                        break;
                    default:
                        netManager.deletePlan(historyOfChanges.get(key));
                        isSyncStarted = false;
                        break;
                }
            }
        } else {
            Cursor cursor = dbManager.getPlans();
            if (!cursor.moveToFirst()) {
                final ArrayList<Plan> plans = new ArrayList<>();
                netManager.getAllPlans(new OnGetPlansCallback() {
                    @Override
                    public void getPlans(ArrayList<Plan> lPlans) {
                        for (Plan plan : lPlans) {
                            plans.add(plan);
                            dbManager.saveNewPlan(plan);
                        }
                        RestartManager restartManager = new RestartManager(ctxt);
                        restartManager.Reload();
                    }
                });
            }
        }
        historyOfChanges.clear();
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
                parseObject.put("audioDuration", plan.getAudioDuration());
                parseObject.put("details", plan.getDetails());
                parseObject.put("userId", ParseUser.getCurrentUser().getObjectId());
                parseObject.saveInBackground();
            }
        }
    }
}

