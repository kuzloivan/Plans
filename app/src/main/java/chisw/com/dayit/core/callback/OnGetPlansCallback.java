package chisw.com.dayit.core.callback;


import java.util.ArrayList;

import chisw.com.dayit.model.Plan;

/**
 * Created by vdboo_000 on 25.06.2015.
 */
public interface OnGetPlansCallback {
    void getPlans(ArrayList<Plan> lPlans);
}
