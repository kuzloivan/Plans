package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chisw.com.plans.R;

public class MediaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Clicker clicker = new Clicker();
        findViewById(R.id.ma_goback_btn).setOnClickListener(clicker);
    }

    public static void start(Activity activity) {
        Intent intnt = new Intent(activity, MediaActivity.class);
        activity.startActivity(intnt);
    }
    public final class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.ma_goback_btn:
                    MediaActivity.this.finish();
            }
        }
    }
}
