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
        CV.put(PlansEntity.AUDIO_DURATION, model.getAudioDuration());
        CV.put(PlansEntity.IMAGE_PATH, model.getImagePath());

        return CV;
    }

    public static ContentValues parseNullablePlan(Plan model) {
        ContentValues CV = new ContentValues();

        if(model.getParseId() != null )
            CV.put(PlansEntity.PARSE_ID, model.getParseId());

        if(model.getTitle() != null )
            CV.put(PlansEntity.TITLE, model.getTitle());

        if(model.getDetails() != null )
            CV.put(PlansEntity.DETAILS, model.getDetails());

        if(model.getTimeStamp() != -1 )
            CV.put(PlansEntity.TIMESTAMP, model.getTimeStamp());

        if(model.getAudioPath() != null )
            CV.put(PlansEntity.AUDIO_PATH, model.getAudioPath());

        if(model.getAudioDuration() != -1 )
            CV.put(PlansEntity.AUDIO_DURATION, model.getAudioDuration());

        if(model.getImagePath() != null )
            CV.put(PlansEntity.IMAGE_PATH, model.getImagePath());
        return CV;
    }

    public static Plan parseCursor(Cursor cursor){
        Plan plan = new Plan();

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int timeStampIndex = cursor.getColumnIndex(PlansEntity.TIMESTAMP);
        int localIdIndex = cursor.getColumnIndex(PlansEntity.LOCAL_ID);
        int parseIdIndex = cursor.getColumnIndex(PlansEntity.PARSE_ID);
        int detailsIndex = cursor.getColumnIndex(PlansEntity.DETAILS);
        int audioPathIndex = cursor.getColumnIndex(PlansEntity.AUDIO_PATH);
        int audioDuration = cursor.getColumnIndex(PlansEntity.AUDIO_DURATION);
        int imagePathIndex = cursor.getColumnIndex(PlansEntity.IMAGE_PATH);

        long timeStamp = cursor.getLong(timeStampIndex);

        plan.setParseId(cursor.getString(parseIdIndex));
        plan.setLocalId(cursor.getInt(localIdIndex));
        plan.setTimeStamp(timeStamp);
        plan.setTitle(cursor.getString(titleIndex));
        plan.setDetails(cursor.getString(detailsIndex));
        plan.setAudioPath(cursor.getString(audioPathIndex));
        plan.setAudioDuration(cursor.getInt(audioDuration));
        plan.setImagePath(cursor.getString(imagePathIndex));
        return plan;
    }

    public static ContentValues parseUser(ParseUser user) {
        ContentValues CV = new ContentValues();

        CV.put(UserEntity.NAME, user.getUsername());
        CV.put(UserEntity.PARSE_ID, user.getObjectId());

        return CV;
    }
}
