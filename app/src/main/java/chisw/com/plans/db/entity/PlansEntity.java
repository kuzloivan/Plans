package chisw.com.plans.db.entity;

/**
 * Created by Darina on 17.06.2015.
 */
public class PlansEntity {
    public static final String TABLE_NAME = "Plans";
    public static final String PARSE_ID = "parseId";
    public static final String TITLE = "title";
    public static final String TIMESTAMP = "timeStamp";
    public static final String LOCAL_ID = "_id";
    public static final String AUDIO_PATH = "audioPath";

    public static final String CREATE_SCRIPT = "Create table " + TABLE_NAME +
            " ( " +
            LOCAL_ID + " INT PRIMARY KEY, " +
            PARSE_ID + " TEXT, " +
            TITLE + " TEXT, " +
            TIMESTAMP + " INT, " +
            AUDIO_PATH + " TEXT " +
            ")";

}
