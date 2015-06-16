package chisw.com.plans.core;

import android.app.Application;

import com.parse.Parse;

public class PApplication extends Application {

    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "fYItmKEFfg4ZxDEB1SrwVMUx82sw91XMyTeZJ0fC", "QVWnE2OAOKVt5yv3KRt830rguZv22wkk8ySkLA4K");
    }

}
