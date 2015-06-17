package chisw.com.plans.db;

import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.core.bridge.DbBridge;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 17.06.2015.
 */
public class DBManager implements DbBridge {

    private List<Plan> plansArray;

    public DBManager(){
        plansArray = new ArrayList<>();
    }

    @Override
    public void saveNewPlan(Plan pPlan) {
        plansArray.add(pPlan);
    }

    @Override
    public List<Plan> getAllPlans() {
        return plansArray;
    }

    @Override
    public void saveMe(ParseUser pParseUser) {

    }

    @Override
    public ParseUser getMe() {
        return null;
    }
}
