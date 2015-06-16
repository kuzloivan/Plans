package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import chisw.com.plans.R;

public class MediaActivity extends ToolbarActivity {

    private TextView message;
    private MediaPlayer player;
    private String path;

    static final int REQUEST_AUDIO_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Clicker clicker = new Clicker();

        getSupportActionBar().setTitle(R.string.title_activity_media);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        findViewById(R.id.ma_choose_btn).setOnClickListener(clicker);
        findViewById(R.id.ma_play_btn).setOnClickListener(clicker);
        findViewById(R.id.ma_stop_btn).setOnClickListener(clicker);

        message = (TextView) findViewById(R.id.ma_res_tv);

        player = new MediaPlayer();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_media;
    }

    public static void start(Activity activity) {
        Intent intnt = new Intent(activity, MediaActivity.class);
        activity.startActivity(intnt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                path = data.getDataString();
                message.setText(path);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ma_choose_btn:
                    chooseAudio();
                    break;
                case R.id.ma_play_btn:
                    startPlayer();
                    break;
                case R.id.ma_stop_btn:
                    stopPlayer();
                    break;
            }
        }

        private void chooseAudio() {
            Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
            chooseAudio.setType("audio/*");
            if (chooseAudio.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
            }
        }

        private void startPlayer() {
            try {
                if (path == null) {
                    message.setText("File wasn't chosen");
                    return;
                }
                player.setDataSource(path);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPlayer() {
        player.stop();
        player.reset();
    }
}
