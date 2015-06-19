package chisw.com.plans.others;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import java.io.IOException;
import chisw.com.plans.core.SharedHelper;

/**
 * Created by Kos on 17.06.2015.
 */
public class Multimedia {
    private int PLAYING_AUDIO_TIME = 15;

    private MediaPlayer player;

    public void startPlayer(String path) {
        try {
            if(player == null){
                player = new MediaPlayer();
            }
            if (player.isPlaying()) {
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

    public void alarmNontification(String path) {
        startPlayer(path);
        Handler h = new Handler();
        Runnable stopPlaybackRun = new Runnable() {
            public void run() {
                player.stop();
                player.release();
                player.reset();
                player = null;
            }
        };
        h.postDelayed(stopPlaybackRun, PLAYING_AUDIO_TIME * 1000);
    }

    public void stopPlayer() {
        player.stop();
        player.release();
        player.reset();
        player = null;
    }

    private final class AudioEndCallback implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlayer();
        }
    }
}

