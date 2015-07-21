package chisw.com.dayit.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.parse.ParseUser;

import chisw.com.dayit.core.bridge.DbBridge;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.db.entity.UserEntity;
import chisw.com.dayit.model.Plan;

/**
 * Created by Alexander on 17.06.2015.
 */

public class DBManager extends java.util.Observable implements DbBridge {

    private DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    //returns every plan from plans_database SQL
    @Override
    public Cursor getPlans() {
        return sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, null, null, null, null, null);
    }

    //get only not deleted plans
    @Override
    public Cursor getNotDeletedPlans() {
        return sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.IS_DELETED + "=?",
                new String[]{String.valueOf(0)}, null, null, null);
    }

    //clears all writings in plans_database SQL
    @Override
    public void deletePlans() {
        sqLiteDatabase.delete(PlansEntity.TABLE_NAME, null, null);
        dbChanged();
    }

    @Override
    public Plan getPlanById(int id) {
        Plan plan = null;
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            plan = Mapper.parseCursor(cursor);
            cursor.close();
        }
        return plan;
    }

    @Override
    public Plan getPlanByTitleAndSender(String pTitle, String pSender, long pTime) {
        Plan plan = null;
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.TITLE + "=? AND " + PlansEntity.SENDER + "=? AND " + PlansEntity.TIMESTAMP + "=?",
                new String[]{pTitle, pSender, Long.toString(pTime)}, null, null, null);
        if(cursor.moveToFirst()) {
            plan = Mapper.parseCursor(cursor);
            cursor.close();
        }
        return plan;
    }

    @Override
    public Plan getPlanByParseId(String pParseId) {
        Plan plan = null;
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.PARSE_ID + "=?",
                new String[]{pParseId}, null, null, null);
        if(cursor.moveToFirst()) {
            plan = Mapper.parseCursor(cursor);
            cursor.close();
        }
        return plan;
    }

    @Override
    public Cursor getCursorById(int id) {
        return sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
    }

    @Override
    public void deletePlanById(long id) {
        sqLiteDatabase.delete(PlansEntity.TABLE_NAME, PlansEntity.LOCAL_ID + "=?", new String[]{String.valueOf(id)});
        dbChanged();
    }

    //erase 1 user by id in user_database SQL
    public void eraseMe(String id) {
        String selection = UserEntity.PARSE_ID + "=?";
        String[] selectionaArgs = new String[]{id};
        sqLiteDatabase.delete(UserEntity.TABLE_NAME, selection, selectionaArgs);
        dbChanged();
    }

    //insert new plan into plans_database SQL
    @Override
    public void saveNewPlan(Plan pPlan) {
        sqLiteDatabase.insert(PlansEntity.TABLE_NAME, null, Mapper.parsePlan(pPlan));
        dbChanged();
    }

    @Override
    public void editPlan(Plan pPlan, int id) {
        sqLiteDatabase.update(PlansEntity.TABLE_NAME, Mapper.parseNullablePlan(pPlan), PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)});
        dbChanged();
    }

    //insert new user into user_database SQL
    @Override
    public void saveMe(ParseUser pParseUser) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.insert(UserEntity.TABLE_NAME, null, Mapper.parseUser(pParseUser));
        dbChanged();
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
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, null, null, null, null, null);
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
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(PlansEntity.TITLE));
        cursor.close();
        return title;
    }

    @Override
    public String getDetailsByID(int id) {
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        String details = cursor.getString(cursor.getColumnIndex(PlansEntity.DETAILS));
        cursor.close();
        return details;
    }

    @Override
    public String getAudioPathByID(int id) {
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(PlansEntity.AUDIO_PATH));
        cursor.close();
        return path;
    }

    @Override
    public int getAudioDurationByID(int id) {
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        int duration = cursor.getInt(cursor.getColumnIndex(PlansEntity.AUDIO_DURATION));
        cursor.close();
        return duration;
    }

    public String getDaysToAlarmById(int id){ //DOW
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        String daysOfWeek = cursor.getString(cursor.getColumnIndex(PlansEntity.DAYS_TO_ALARM));
        cursor.close();
        return daysOfWeek;
    }
    @Override
    public String getPicturePathByID(int id){
        Cursor cursor = sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, PlansEntity.LOCAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(PlansEntity.IMAGE_PATH));
        return path;
    }

    public Cursor getAllContacts(Context ctx){
        Cursor cursor = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

        return cursor;
    }

    @Override
    public void dbChanged() {
        setChanged();
        notifyObservers();
    }
}
