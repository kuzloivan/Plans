package chisw.com.plans.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import chisw.com.plans.R;

public class MediaActivity extends ToolbarActivity {

    private static final int REQUEST_AUDIO_GET = 1;
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    private TextView message;
    private MediaPlayer player;
    private String path;
    private AudioEnd aEnd;
    private boolean isChAudioExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aEnd = new AudioEnd();
        Clicker clicker = new Clicker();
        findViewById(R.id.ma_choose_btn).setOnClickListener(clicker);
        findViewById(R.id.ma_play_btn).setOnClickListener(clicker);
        findViewById(R.id.ma_stop_btn).setOnClickListener(clicker);
        player = new MediaPlayer();

        getSupportActionBar().setTitle(R.string.title_activity_media); //todo set title in manifest
        initBackButton();

        message = (TextView) findViewById(R.id.ma_res_tv);
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
            isChAudioExist = false;
            return;
        }
        switch (requestCode) {
            case REQUEST_AUDIO_GET:
                path = getPath(data);
                message.setText(path);
                isChAudioExist = false;
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public final class AudioEnd implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlayer();
        }
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
            if (isChAudioExist) {
                return;
            }
            isChAudioExist = true;
            Intent chooseAudio = new Intent(Intent.ACTION_GET_CONTENT);
            chooseAudio.setType("audio/*");
            if (chooseAudio.resolveActivity(getPackageManager()) == null) {
                isChAudioExist = false;
            }
            startActivityForResult(chooseAudio, REQUEST_AUDIO_GET);
        }

        private void startPlayer() {
            try {
                if (player.isPlaying()) {
                    return;
                }
                if (path == null) {
                    message.setText("File wasn't chosen");
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
    }

    private void stopPlayer() {
        player.stop();
        player.reset();
    }

    public String getPath(Intent str) {
        if (isKitKat) {
            Uri data = str.getData();
            final String docId = DocumentsContract.getDocumentId(data);
            final String[] split = docId.split(":");
            Uri contentUri = null;
            if ("com.android.providers.media.documents".equals(data.getAuthority())) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    split[1]
            };
            return getDataColumn(this, contentUri, selection, selectionArgs);
        } else {
            return str.getDataString();
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
