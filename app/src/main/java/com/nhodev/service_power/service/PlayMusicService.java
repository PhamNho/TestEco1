package com.nhodev.service_power.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhodev.service_power.MainActivity;
import com.nhodev.service_power.R;
import com.nhodev.service_power.utils.NotificationConst;

import java.io.IOException;

/**
 * Được tạo bởi Phạm Nhớ ngày 01/11/2021.
 **/
public class PlayMusicService extends Service {
    private String TAG = "MyService";
    private int length = 0;
    private Context context;
    private MediaPlayer mMediaPlayer = null;
    NotificationBarHelper notificationBarHelper;

    public PlayMusicService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        context = PlayMusicService.this;
        notificationBarHelper = new NotificationBarHelper(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            createNotificationChannel();
            int action = intent.getIntExtra("action", 0);
            String pathMusic = intent.getStringExtra("pathMusic");
            Log.d(TAG, "onStartCommand: " + action);
            if (pathMusic != null) {
                playMusic(pathMusic);
            }
            switch (action) {
                case NotificationConst.ACTION_PAUSE:
                    pauseMusic();
                    break;
                case NotificationConst.ACTION_RE_PLAY:
                    resumeMusic();
                    break;
                default:
                    playMusic(pathMusic);
                    break;
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    public void playMusic(String path) {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                try {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(path));
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(path));
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(path));
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            length = mMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(length);
            mMediaPlayer.start();
        }
    }

    public void stopMusic() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
        return false;
    }

    private void createNotificationChannel() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel_id_1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, mBuilder.build());
    }
}
