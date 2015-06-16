package chisw.com.plans;

import android.app.Application;
import android.content.res.Configuration;
import android.test.ApplicationTestCase;

import com.parse.Parse;


public class ApplicationTest extends Application {

    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "fYItmKEFfg4ZxDEB1SrwVMUx82sw91XMyTeZJ0fC", "QVWnE2OAOKVt5yv3KRt830rguZv22wkk8ySkLA4K");
    }
}