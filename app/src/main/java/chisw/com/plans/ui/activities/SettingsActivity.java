package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import chisw.com.plans.R;


public class SettingsActivity extends ToolbarActivity {
    public static boolean Coose_user_audio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Switcher sw = new Switcher();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CompoundButton cb = (CompoundButton) findViewById(R.id.sa_set_user_sound);
        cb.setChecked(false);
        cb.setOnCheckedChangeListener(sw);
    }

    @Override
    protected void onStop(){
        super.onStop();
        showToast("onStop");
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_settings;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public final class Switcher implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1){

            Coose_user_audio = arg1;
            showToast("Coose_user_audio = "+ Coose_user_audio);
        }
    }

}

