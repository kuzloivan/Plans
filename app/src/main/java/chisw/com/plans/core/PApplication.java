package chisw.com.plans.core;

import android.app.Application;
import android.media.MediaPlayer;

import com.parse.Parse;

import chisw.com.plans.db.DBManager;
import chisw.com.plans.net.NetManager;
import chisw.com.plans.others.Multimedia;

public class PApplication extends Application {

    private NetManager netManager;
    private DBManager dbManager;
    private SharedHelper sharedHelper;

    private static final String APP_KEY = "fYItmKEFfg4ZxDEB1SrwVMUx82sw91XMyTeZJ0fC";
    private static final String CLNT_KEY = "QVWnE2OAOKVt5yv3KRt830rguZv22wkk8ySkLA4K";
    private SharedHelper sharedHelper;
    private Multimedia multimedia;

    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APP_KEY, CLNT_KEY);

        netManager = new NetManager();
        sharedHelper = new SharedHelper(this);
        multimedia = new Multimedia(sharedHelper);
        dbManager = new DBManager(this);
    }

    public NetManager getNetManager() {
        return netManager;
    }

    public SharedHelper getSharedHelper() {
        return sharedHelper;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public DBManager getDbManager() { return dbManager; }
}
