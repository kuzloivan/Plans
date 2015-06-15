package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import chisw.com.plans.R;

public class NetManagmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_managment);
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagmentActivity.class);
        a.startActivity(intent);
    }
}
