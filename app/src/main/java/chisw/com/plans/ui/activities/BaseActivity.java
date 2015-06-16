package chisw.com.plans.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.net.NetManager;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected NetManager netManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netManager = ((PApplication)getApplication()).getNetManager();
    }
}
