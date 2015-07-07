package chisw.com.dayit.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.core.SharedHelper;
import chisw.com.dayit.db.AlarmManager;
import chisw.com.dayit.db.DBManager;
import chisw.com.dayit.net.NetManager;
import chisw.com.dayit.others.Multimedia;

public abstract class BaseActivity extends AppCompatActivity {

    protected NetManager netManager;
    protected AlarmManager alarmManager;
    protected SharedHelper sharedHelper;
    protected Multimedia multimedia;
    protected DBManager dbManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication pApplication = (PApplication) getApplication();

        alarmManager = pApplication.getAlarmManager();
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
        progressDialog.setCancelable(false);
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
