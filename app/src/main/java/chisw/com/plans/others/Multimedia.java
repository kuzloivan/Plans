package chisw.com.plans.others;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.io.IOException;


/**
 * Created by Kos on 17.06.2015.
 */

public class Multimedia {
    private int PLAYING_AUDIO_TIME = 10;
    private int playTime;

    public void setPlayTime(int playTime) {
        if (playTime < 10) {
            this.playTime = PLAYING_AUDIO_TIME;
        } else {
            this.playTime = playTime;
        }
    }

    private MediaPlayer player;

    public void startPlayer(String path) {
        try {
            player.setDataSource(path);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new AudioEndCallback());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alarmNotification(String path) {
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player.isPlaying()) {
            player.stop();
            player.reset();
            startPlayer(path);
            return;
        } else {
            startPlayer(path);
        }
        Handler h = new Handler();
        Runnable stopPlaybackRun = new Runnable() {
            public void run() {
                stopPlayer();
            }
        };
        h.postDelayed(stopPlaybackRun, playTime * 1000);
    }

    public void stopPlayer() {
        if(player == null){
            return;
        }
        player.stop();
        player.reset();
        player.release();
        player = null;
    }

    private final class AudioEndCallback implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlayer();
        }
    }
}


