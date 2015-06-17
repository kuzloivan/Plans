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

    private SharedHelper sharedHelper;
    private MediaPlayer player;
    private AudioEnd aEnd;
    private String path;
    private int seconds;

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setPath(String path) {
        this.path = path;
        sharedHelper.setDefaultMediaWay(path);
    }

    public Multimedia(SharedHelper sharedHelper) {
        this.sharedHelper = sharedHelper;
        player = new MediaPlayer();
        aEnd = new AudioEnd();
        path = sharedHelper.getDefaultMediaWay();
    }

    public void startPlayer() {
        try {
            if (player.isPlaying()) {
                return;
            }
            if (path == null || path == "") {
                // message.setText("File wasn't chosen");
                return;
            }
            player.setDataSource(path);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            player.start();
            player.setOnCompletionListener(aEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alarmNontification() {
        startPlayer();
        Handler h = new Handler();
        Runnable stopPlaybackRun = new Runnable() {
            public void run() {
                player.stop();
                player.reset();
            }
        };
        h.postDelayed(stopPlaybackRun, 15 * 1000);
        //h.postDelayed(stopPlaybackRun, seconds * 1000);

    }

    public void stopPlayer() {
        player.stop();
        player.reset();
    }

    public final class AudioEnd implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlayer();
        }
    }
}
