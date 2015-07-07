package chisw.com.dayit.db.entity;

/**
 * Created by Oksana on 17.06.2015.
 */

public class UserEntity {
    public static final String TABLE_NAME = "User";
    public static final String PARSE_ID = "parseId";
    public static final String NAME = "name";

    public static final String CREATE_SCRIPT = "Create table " + TABLE_NAME +
            " ( " +
            "_id INT PRIMARY KEY, " +
            PARSE_ID + " TEXT, " +
            NAME + " TEXT " +
            ")";

}
