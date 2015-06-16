package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseException;

import chisw.com.plans.R;
import chisw.com.plans.net.NetManager;

public class NetManagementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_management);

        ClickerNet clickerNet = new ClickerNet();
        findViewById(R.id.nm_ret_btn).setOnClickListener(clickerNet);
    }

    public final class ClickerNet implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.nm_ret_btn:
                    NetManager netManager = new NetManager();
                    try {
                        netManager.registerUser("vlad", "123456");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static void start(Activity a) {
        Intent intent = new Intent(a, NetManagementActivity.class);
        a.startActivity(intent);
    }
}
