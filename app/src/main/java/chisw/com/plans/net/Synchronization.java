package chisw.com.plans.net;

import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.bridge.OnSaveCallback;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.model.Plan;
import chisw.com.plans.ui.activities.LogInActivity;

/**
 * Created by vdboo_000 on 23.06.2015.
 */
public class Synchronization {
    public  Map<Integer, Integer> historyOfChanges;
    private DBManager dbManager;
    private NetManager netManager;

    public Synchronization(DBManager dbManager, NetManager netManager) {
        this.historyOfChanges = new HashMap<>();
        this.dbManager = dbManager;
        this.netManager = netManager;
    }

    public void wasAdding(int planId) {
        historyOfChanges.put(planId, 0);
    }

    public void wasEditing(int planId) {
        historyOfChanges.put(planId, 1);
    }

    public void wasDeleting(int planId) {
        historyOfChanges.put(planId, 2);
    }

    public void startSynchronization() {
        for(Integer key : historyOfChanges.keySet()) {
            final Plan plan = dbManager.getPlanById(key);
            switch(historyOfChanges.get(key)) {
                case 0:
                    netManager.addPlan(plan, new OnSaveCallback() {
                        @Override
                        public void getId(String id) {
                            plan.setParseId(id);
                            int planId = dbManager.getPlanById(dbManager.getLastPlanID()).getLocalId();
                            dbManager.editPlan(plan, planId);
                        }
                    });
                    break;
                case 1:
                    break;
                case 2:
                    if(plan.getParseId()!=""){
                        netManager.deletePlan(plan.getParseId());
                    }
                    break;
            }
        }
    }
}

