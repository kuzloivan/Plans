package chisw.com.plans.core.bridge;

import android.database.Cursor;

import com.parse.ParseUser;

import java.util.List;

import chisw.com.plans.model.Plan;

public interface DbBridge {

    void saveNewPlan(Plan pPlan);
    List<Plan> getAllPlans();
    Cursor getPlans();
    void clearPlans();
    Plan selectPlanById(int id);
    void eraseMe(String id);
    void saveMe(ParseUser pParseUser);
    Cursor getMe(String id);
}
