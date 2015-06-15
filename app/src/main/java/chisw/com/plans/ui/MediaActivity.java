package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import chisw.com.plans.R;

public class MediaActivity extends Activity {
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        goBack = (Button) findViewById(R.id.ma_goback_btn);
    }

    public static void Start(Activity activity) {
        Intent intnt = new Intent(activity, MediaActivity.class);
        activity.startActivity(intnt);
    }

}
