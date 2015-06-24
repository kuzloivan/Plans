package chisw.com.plans.db.entity;

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

    public static final String CREATE_SCRIPT = "Create table " + TABLE_NAME +
            " ( " +
            LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PARSE_ID + " TEXT, " +
            TITLE + " TEXT, " +
            DETAILS + " TEXT, " +
            TIMESTAMP + " INTEGER, " +
            AUDIO_PATH + " TEXT, " +
            AUDIO_DURATION + " INTEGER " + //Coma!
//            IMAGE_PATH + " TEXT " +
            ")";

}
