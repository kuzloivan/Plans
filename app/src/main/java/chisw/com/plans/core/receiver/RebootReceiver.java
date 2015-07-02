package chisw.com.plans.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import chisw.com.plans.others.RestartManager;


/**
 * Created by Yuriy on 16.06.2015.
 */
public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            RestartManager rm = new RestartManager(ctx);
            rm.reload();
        }
    }
}