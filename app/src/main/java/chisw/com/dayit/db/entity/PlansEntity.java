package chisw.com.dayit.db.entity;

/**
 * Created by Darina on 17.06.2015.
 */

public class PlansEntity {
    public static final String TABLE_NAME = "Plans";
    public static final String PARSE_ID = "parseId";
    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String TIMESTAMP = "timeStamp";
    public static final String LOCAL_ID = "_id";
    public static final String AUDIO_PATH = "audioPath";
    public static final String AUDIO_DURATION = "audioDuration";
    public static final String IMAGE_PATH = "imagePath";
    public static final String DAYS_TO_ALARM = "daysToAlarm";
    public static final String IS_DELETED = "isDeleted";
    public static final String IS_SYNCHRONIZED = "isSynchronized";

    public static final String CREATE_SCRIPT = "Create table " + TABLE_NAME +
            " ( " +
            LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PARSE_ID + " TEXT, " +
            TITLE + " TEXT, " +
            DETAILS + " TEXT, " +
            TIMESTAMP + " INTEGER, " +
            AUDIO_PATH + " TEXT, " +
            AUDIO_DURATION + " INTEGER, " +
            IMAGE_PATH + " TEXT, " +
            DAYS_TO_ALARM + " TEXT, " + //Coma!   DOW
            IS_DELETED + " INTEGER, " +
            IS_SYNCHRONIZED + " INTEGER " +
            ")";
}
