package chisw.com.plans.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import chisw.com.plans.R;

public class MediaActivity extends Activity {

    static final int REQUEST_AUDIO_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Clicker clicker = new Clicker();
        findViewById(R.id.ma_goback_btn).setOnClickListener(clicker);
        findViewById(R.id.ma_choose_btn).setOnClickListener(clicker);
    }

    public static void start(Activity activity) {
        Intent intnt = new Intent(activity, MediaActivity.class);
        activity.startActivity(intnt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ma_goback_btn:
                    MediaActivity.this.finish();
                    break;
                case R.id.ma_choose_btn:
                    Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseAudio.setType("audio/*");
                    if (chooseAudio.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
                    }
                    break;

            }
        }
    }
}
