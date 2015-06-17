package chisw.com.plans.db.entity;

/**
 * Created by Darina on 17.06.2015.
 */
public class PlansEntity {
    public static final String TABLE_NAME = "Plans";
    public static final String PARSE_ID = "parseId";
    public static final String NAME = "name";
    public static final String TIMESTAMP = "timeStamp";

    public static final String CREATE_SCRIPT = "Create table " + TABLE_NAME +
            " ( " +
            "_id INT PRIMARY KEY, " +
            PARSE_ID + " TEXT, " +
            NAME + " TEXT, " +
            TIMESTAMP + " TEXT " +
            ")";

}
