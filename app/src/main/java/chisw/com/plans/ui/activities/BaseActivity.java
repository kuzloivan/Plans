package chisw.com.plans.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.SharedHelper;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.net.NetManager;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected NetManager netManager;
    protected SharedHelper sharedHelper;
    protected DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication pApplication = (PApplication) getApplication();

        sharedHelper = pApplication.getSharedHelper();
        netManager = pApplication.getNetManager();
        dbManager = pApplication.getDbManager();
    }

    protected void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
