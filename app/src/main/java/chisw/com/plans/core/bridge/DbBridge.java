package chisw.com.plans.core.bridge;

import android.database.Cursor;

import com.parse.ParseUser;

import java.util.List;

import chisw.com.plans.model.Plan;

public interface DbBridge {

    void saveNewPlan(Plan pPlan);
    void editPlan(Plan pPlan);
    Cursor getPlans();
    void clearPlans();
    Plan getPlanById(int id);
    void deletePlanById(long id);
    void eraseMe(String id);
    void saveMe(ParseUser pParseUser);
    Cursor getMe(String id);
    int getLastPlanID();
    String getTitleByID(int id);
    String getDetailsByID(int id);
    String getAudioPathByID(int id);
    void dbChanged(); // should be called when database content was changed
    int getAudioDurationByID (int id);
}
