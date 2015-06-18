package chisw.com.plans.others;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;

import chisw.com.plans.core.SharedHelper;

/**
 * Created by Kos on 17.06.2015.
 */
public class Multimedia {
    private int PLAYING_AUDIO_TIME = 15;

    private MediaPlayer player;
    private SharedHelper mSharedHelper;

    public Multimedia(SharedHelper sharedHelper) {
        mSharedHelper = sharedHelper;
    }

    public void startPlayer() {

        String path = mSharedHelper.getDefaultMediaWay();

        try {

            if(player == null){
                player = new MediaPlayer();
            }

            if (player.isPlaying() || TextUtils.isEmpty(path)) {
                return;
            }

            player.setDataSource(path);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new AudioEndCallback());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alarmNontification(Context context) {
        String path = mSharedHelper.getDefaultMediaWay();

        if (TextUtils.isEmpty(path)) {
            Toast.makeText(context, "Audio wasn't chosen", Toast.LENGTH_SHORT);
            return;
        }
        startPlayer();
        Handler h = new Handler();
        Runnable stopPlaybackRun = new Runnable() {
            public void run() {
                player.stop();
                player.reset();
                player = null;
            }
        };
        h.postDelayed(stopPlaybackRun, PLAYING_AUDIO_TIME * 1000);
    }

    public void stopPlayer() {
        player.stop();
        player.reset();
    }

    private final class AudioEndCallback implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlayer();
        }
    }

}

