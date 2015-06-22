package chisw.com.plans.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.parse.ParseUser;

import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.db.entity.UserEntity;
import chisw.com.plans.model.Plan;

/**
 * Created by andrey on 17.06.15.
 */

public class Mapper {

    public static ContentValues parsePlan(Plan model) {
        ContentValues CV = new ContentValues();
        CV.put(PlansEntity.PARSE_ID, model.getParseId());
        CV.put(PlansEntity.TITLE, model.getTitle());
        CV.put(PlansEntity.DETAILS, model.getDetails());
        CV.put(PlansEntity.TIMESTAMP, model.getTimeStamp());
        CV.put(PlansEntity.AUDIO_PATH, model.getAudioPath());

        return CV;
    }

    public static Plan parseCursor(Cursor cursor){
        Plan plan = new Plan();

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int timeStampIndex = cursor.getColumnIndex(PlansEntity.TIMESTAMP);
        int parseIdIndex = cursor.getColumnIndex(PlansEntity.PARSE_ID);
        int detailsIndex = cursor.getColumnIndex(PlansEntity.DETAILS);

        long timeStamp = cursor.getLong(timeStampIndex);

        plan.setParseId(cursor.getString(parseIdIndex));
        plan.setTimeStamp(timeStamp);
        plan.setTitle(cursor.getString(titleIndex));
        plan.setDetails(cursor.getString(detailsIndex));

        return plan;
    }

    public static ContentValues parseUser(ParseUser user) {
        ContentValues CV = new ContentValues();

        CV.put(UserEntity.NAME, user.getUsername());
        CV.put(UserEntity.PARSE_ID, user.getObjectId());

        return CV;
    }
}
