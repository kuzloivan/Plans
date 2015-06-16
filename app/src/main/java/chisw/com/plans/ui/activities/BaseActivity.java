package chisw.com.plans.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.SharedHelper;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected NetManager netManager;

    protected SharedHelper sharedHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication pApplication = (PApplication) getApplication();
        sharedHelper = pApplication.getSharedHelper();
    }

    protected void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netManager = ((PApplication)getApplication()).getNetManager();
    }
}
