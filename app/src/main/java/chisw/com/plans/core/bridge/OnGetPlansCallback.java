package chisw.com.plans.core.bridge;

import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.model.Plan;

/**
 * Created by vdboo_000 on 25.06.2015.
 */
public interface OnGetPlansCallback {
    void getPlans(ArrayList<Plan> lPlans);
}
