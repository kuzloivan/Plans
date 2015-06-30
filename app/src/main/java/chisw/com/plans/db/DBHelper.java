package chisw.com.plans.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.db.entity.UserEntity;

/**
 * Created by Darina on 17.06.2015.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String NAME = "plans.db";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PlansEntity.CREATE_SCRIPT);
        sqLiteDatabase.execSQL(UserEntity.CREATE_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
