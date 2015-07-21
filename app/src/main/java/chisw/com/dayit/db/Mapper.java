package chisw.com.dayit.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.parse.ParseUser;

import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.db.entity.UserEntity;
import chisw.com.dayit.model.Plan;

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
        CV.put(PlansEntity.DAYS_TO_ALARM, model.getDaysToAlarm());
        CV.put(PlansEntity.IS_DELETED, model.getIsDeleted());
        CV.put(PlansEntity.IS_SYNCHRONIZED, model.getIsSynchronized());
        CV.put(PlansEntity.UPDATED_AT_PARSE_TIME, model.getUpdatedAtParseTime());
        CV.put(PlansEntity.IS_REMOTE, model.getIsRemote());
        CV.put(PlansEntity.SENDER, model.getSender());
        CV.put(PlansEntity.PLAN_STATE, model.getPlanState());
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

        if(model.getDaysToAlarm() != null )
            CV.put(PlansEntity.DAYS_TO_ALARM, model.getDaysToAlarm());

        if(model.getIsDeleted() != -1 )
            CV.put(PlansEntity.IS_DELETED, model.getIsDeleted());

        if(model.getIsSynchronized() != -1 )
            CV.put(PlansEntity.IS_SYNCHRONIZED, model.getIsSynchronized());

        if(model.getUpdatedAtParseTime() != -1)
            CV.put(PlansEntity.UPDATED_AT_PARSE_TIME, model.getUpdatedAtParseTime());

        if(model.getIsRemote() != -1)
            CV.put(PlansEntity.IS_REMOTE, model.getIsRemote());

        if(model.getSender() != null)
            CV.put(PlansEntity.SENDER, model.getSender());

        if(model.getPlanState() != null)
            CV.put(PlansEntity.PLAN_STATE, model.getPlanState());

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
        int daysToAlarm = cursor.getColumnIndex(PlansEntity.DAYS_TO_ALARM);
        int isDeletedIndex = cursor.getColumnIndex(PlansEntity.IS_DELETED);
        int isSynchronizedIndex = cursor.getColumnIndex(PlansEntity.IS_SYNCHRONIZED);
        int changedAtParseTimeIndex = cursor.getColumnIndex(PlansEntity.UPDATED_AT_PARSE_TIME);
        int isRemoteIndex = cursor.getColumnIndex(PlansEntity.IS_REMOTE);
        int senderIndex = cursor.getColumnIndex(PlansEntity.SENDER);
        int planStateIndex = cursor.getColumnIndex(PlansEntity.PLAN_STATE);

        long timeStamp = cursor.getLong(timeStampIndex);

        plan.setParseId(cursor.getString(parseIdIndex));
        plan.setLocalId(cursor.getInt(localIdIndex));
        plan.setTimeStamp(timeStamp);
        plan.setTitle(cursor.getString(titleIndex));
        plan.setDetails(cursor.getString(detailsIndex));
        plan.setAudioPath(cursor.getString(audioPathIndex));
        plan.setAudioDuration(cursor.getInt(audioDuration));
        plan.setImagePath(cursor.getString(imagePathIndex));
        plan.setDaysToAlarm(cursor.getString(daysToAlarm));
        plan.setIsDeleted(cursor.getInt(isDeletedIndex));
        plan.setIsSynchronized(cursor.getInt(isSynchronizedIndex));
        plan.setUpdatedAtParseTime(cursor.getLong(changedAtParseTimeIndex));
        plan.setIsRemote(cursor.getInt(isRemoteIndex));
        plan.setSender(cursor.getString(senderIndex));
        plan.setPlanState(cursor.getString(planStateIndex));
        return plan;
    }

    public static ContentValues parseUser(ParseUser user) {
        ContentValues CV = new ContentValues();

        CV.put(UserEntity.NAME, user.getUsername());
        CV.put(UserEntity.PARSE_ID, user.getObjectId());

        return CV;
    }
}
