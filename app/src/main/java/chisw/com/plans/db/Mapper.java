package chisw.com.plans.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.model.Plan;

/**
 * Created by andrey on 17.06.15.
 */
public class Mapper {

    public static ContentValues parsePlan(Plan model) {
        ContentValues CV = new ContentValues();

        CV.put(PlansEntity.PARSE_ID, model.getParseId());
        CV.put(PlansEntity.TITLE, model.getTitle());
        CV.put(PlansEntity.TIMESTAMP, model.getTimeStamp());

        return CV;
    }

}
