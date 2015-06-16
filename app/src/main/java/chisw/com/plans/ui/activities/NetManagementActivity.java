package chisw.com.plans.ui.activities;

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

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.nm_reg_btn).setOnClickListener(clickerNet);
        findViewById(R.id.nm_log_btn).setOnClickListener(clickerNet);
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_net_management;
    }

    public final class ClickerNet implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.nm_reg_btn:

                    break;
                case R.id.nm_log_btn:

                    break;
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagementActivity.class);
        a.startActivity(intent);
    }
}
