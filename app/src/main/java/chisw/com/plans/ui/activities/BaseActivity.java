package chisw.com.plans.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import chisw.com.plans.core.PApplication;
import chisw.com.plans.core.SharedHelper;
import chisw.com.plans.db.DBManager;
import chisw.com.plans.net.NetManager;
import chisw.com.plans.others.Multimedia;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected NetManager netManager;
    protected SharedHelper sharedHelper;
    protected Multimedia multimedia;
    protected DBManager dbManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication pApplication = (PApplication) getApplication();

        sharedHelper = pApplication.getSharedHelper();
        netManager = pApplication.getNetManager();
        dbManager = pApplication.getDbManager();
        multimedia = pApplication.getMultimedia();
    }

    protected void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
