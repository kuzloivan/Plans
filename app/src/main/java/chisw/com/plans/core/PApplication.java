package chisw.com.plans.core;

import android.app.Application;

import com.parse.Parse;

import chisw.com.plans.db.AlarmManager;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.net.NetManager;
import chisw.com.plans.net.Synchronization;
import chisw.com.plans.others.Multimedia;

public class PApplication extends Application {

    private NetManager netManager;
    private DBManager dbManager;
    private SharedHelper sharedHelper;
    private Multimedia multimedia;
    private Synchronization synchronization;
    private AlarmManager alarmManagerHelper;

    private static final String APP_KEY = "fYItmKEFfg4ZxDEB1SrwVMUx82sw91XMyTeZJ0fC";
    private static final String CLNT_KEY = "QVWnE2OAOKVt5yv3KRt830rguZv22wkk8ySkLA4K";


    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APP_KEY, CLNT_KEY);

        alarmManagerHelper = new AlarmManager(this);
        netManager = new NetManager();
        sharedHelper = new SharedHelper(this);
        multimedia = new Multimedia();
        dbManager = new DBManager(this);
        synchronization = new Synchronization(dbManager, netManager);
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

    public AlarmManager getAlarmManager() { return alarmManagerHelper; }

    public DBManager getDbManager() { return dbManager; }

    public Synchronization getSynchronization() { return synchronization; }
}
