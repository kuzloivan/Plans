package chisw.com.plans.core.bridge;

import com.parse.ParseUser;

import java.util.List;

import chisw.com.plans.model.Plan;

public interface DbBridge {

    void saveNewPlan(Plan pPlan);
    List<Plan> getAllPlans();

    void saveMe(ParseUser pParseUser);
    ParseUser getMe();
}
