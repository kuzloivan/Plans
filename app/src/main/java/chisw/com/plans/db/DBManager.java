package chisw.com.plans.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.core.bridge.DbBridge;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 17.06.2015.
 */
public class DBManager implements DbBridge {

    private List<Plan> plansArray;
    private DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    public DBManager (Context context)    {
        dbHelper = new DBHelper(context);
        plansArray = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    public List<Plan> getAllPlans() {
        return plansArray;
    }

    @Override
    public Cursor getPlans() {
        return sqLiteDatabase.query(PlansEntity.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public void saveNewPlan(Plan pPlan) {
        plansArray.add(pPlan);

        ContentValues contentValues = new ContentValues();

        sqLiteDatabase.insert(PlansEntity.TABLE_NAME, null, contentValues);
    }

    @Override
    public void saveMe(ParseUser pParseUser) {

    }

    @Override
    public ParseUser getMe() {
        return null;
    }
}
