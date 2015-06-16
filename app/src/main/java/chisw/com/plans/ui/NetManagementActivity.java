package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.net.NetManager;

public class NetManagementActivity extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_management);

    }



    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagementActivity.class);
        a.startActivity(intent);
    }
}
