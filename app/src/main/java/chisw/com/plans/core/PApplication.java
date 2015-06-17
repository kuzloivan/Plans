package chisw.com.plans.core;

import android.app.Application;

import com.parse.Parse;

import chisw.com.plans.db.DBManager;
import chisw.com.plans.net.NetManager;

public class PApplication extends Application {

    private NetManager netManager;
    private DBManager dbManager;
    private SharedHelper sharedHelper;

    private static final String APP_KEY = "fYItmKEFfg4ZxDEB1SrwVMUx82sw91XMyTeZJ0fC";
    private static final String CLNT_KEY = "QVWnE2OAOKVt5yv3KRt830rguZv22wkk8ySkLA4K";

    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APP_KEY, CLNT_KEY);

        netManager = new NetManager();
        sharedHelper = new SharedHelper(this);
        dbManager = new DBManager(this);
    }

    public NetManager getNetManager() {
        return netManager;
    }

    public SharedHelper getSharedHelper() {
        return sharedHelper;
    }

    public DBManager getDbManager() { return dbManager; }
}
