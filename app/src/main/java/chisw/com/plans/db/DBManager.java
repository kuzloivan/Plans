package chisw.com.plans.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.bridge.DbBridge;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.db.entity.UserEntity;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 17.06.2015.
 */
public class DBManager implements DbBridge {

    private List<Plan> plansArray;
    private DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        plansArray = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    //returns list of plans
    @Override
    public List<Plan> getAllPlans() {
        return plansArray;
    }

    //returns every plan from plans_database SQL
    @Override
    public Cursor getPlans() {
        return sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, null, null, null, null, null);
    }

    //clears all writtings in plans_database SQL
    @Override
    public void clearPlans() {
        sqLiteDatabase.delete(PlansEntity.TABLE_NAME, null, null);
    }


    @Override
    public Plan selectPlanById(int id) {

        Plan plan = null;
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {

            plan = Mapper.parseCursor(cursor);
            cursor.close();
        }
        return plan;
    }

    @Override
    public int deletePlanById(long id) {
        return sqLiteDatabase.delete(PlansEntity.TABLE_NAME, PlansEntity.LOCAL_ID + "=?", new String[]{String.valueOf(id)});
    }

    //erase 1 user by id in user_database SQL
    public void eraseMe(String id) {
        String selection = UserEntity.PARSE_ID + "=?";
        String[] selectionaArgs = new String[]{id};
        sqLiteDatabase.delete(UserEntity.TABLE_NAME, selection, selectionaArgs);
    }

    //insert new plan into plans_database SQL
    @Override
    public void saveNewPlan(Plan pPlan) {
        plansArray.add(pPlan);

        sqLiteDatabase.insert(PlansEntity.TABLE_NAME, null, Mapper.parsePlan(pPlan, getLastPlanID()));
    }

    //insert new user into user_database SQL
    @Override
    public void saveMe(ParseUser pParseUser) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.insert(UserEntity.TABLE_NAME, null, Mapper.parseUser(pParseUser));
    }

    //returns user from user_databese SQL
    @Override
    public Cursor getMe(String id) {
        String selection = UserEntity.PARSE_ID + "=?";
        String[] selectionaArgs = new String[]{id};
        return sqLiteDatabase.query(UserEntity.TABLE_NAME, null, selection, selectionaArgs, null, null, null);
    }

    @Override
    public int getLastPlanID() {
       Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, new String[]{PlansEntity.LOCAL_ID, PlansEntity.PARSE_ID, PlansEntity.TITLE, PlansEntity.TIMESTAMP,PlansEntity.AUDIO_PATH}, null, null, null, null, null);
        //Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, new String[]{PlansEntity.LOCAL_ID}, null, null, null, null, null);
        int val;
        if (cursor.getCount() == 0) {
            val = 0;
        } else {
            cursor.moveToLast();
            val = cursor.getInt(cursor.getColumnIndex(PlansEntity.LOCAL_ID));
        }
        cursor.close();
        return val;
    }

    @Override
    public String getTitleByID(int id) {
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, new String[]{PlansEntity.LOCAL_ID, PlansEntity.PARSE_ID, PlansEntity.TITLE, PlansEntity.TIMESTAMP, PlansEntity.AUDIO_PATH}, null, null, null, null, null);
        cursor.moveToPosition(id - 1);
        String title = cursor.getString(cursor.getColumnIndex(PlansEntity.TITLE));
        return title;
    }

    @Override
    public String getAudioPathByID(int id) {
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, new String[]{PlansEntity.LOCAL_ID, PlansEntity.PARSE_ID, PlansEntity.TITLE, PlansEntity.TIMESTAMP,PlansEntity.AUDIO_PATH}, null, null, null, null, null);
        cursor.moveToPosition(id - 1);
        String path = cursor.getString(cursor.getColumnIndex(PlansEntity.AUDIO_PATH));
        return path;
    }
}
