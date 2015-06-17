package chisw.com.plans.db;

import android.content.Context;

import java.util.List;

import chisw.com.plans.core.bridge.DbBridge;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 17.06.2015.
 */
public class DBManager implements DbBridge {

    DBHelper dbHelper;

    public DBManager (Context context)    {
        dbHelper = new DBHelper(context);

    }

    @Override
    public void saveNewPlan(Plan pPlan) {

    }

    @Override
    public List<Plan> getAllPlans() {
        return null;
    }
}
