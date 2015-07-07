package chisw.com.dayit.core.bridge;

import android.database.Cursor;

import com.parse.ParseUser;

import chisw.com.dayit.model.Plan;

public interface DbBridge {

    void saveNewPlan(Plan pPlan);
    void editPlan(Plan pPlan, int id);
    Cursor getPlans();
    Cursor getNotDeletedPlans();
    void clearPlans();
    Plan getPlanById(int id);
    Cursor getCursorById(int id);
    void deletePlanById(long id);
    void eraseMe(String id);
    void saveMe(ParseUser pParseUser);
    Cursor getMe(String id);
    int getLastPlanID();
    String getTitleByID(int id);
    String getDetailsByID(int id);
    String getAudioPathByID(int id);
    String getPicturePathByID(int id);
    void dbChanged(); // should be called when database content was changed
    int getAudioDurationByID (int id);
}
