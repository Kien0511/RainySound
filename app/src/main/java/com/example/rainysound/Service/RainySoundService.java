package com.example.rainysound.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.rainysound.MainActivity;
import com.example.rainysound.MySharedPreference.MySharedPreference;
import com.example.rainysound.MyUtil;
import com.example.rainysound.R;

import java.util.ArrayList;
import java.util.List;

public class RainySoundService extends Service {
    public static final String ACTION_PLAY_PAUSE = "com.example.rainysound.ACTION_PLAY_PAUSE";
    public static final String ACTION_STOP = "com.example.rainysound.ACTION_STOP";
    private final String CHANNEL_ID = "com.example.rainysound.CHANNEL_ID";
    private static final int REQUEST_CODE_STOP = 1114;
    private static final int REQUEST_CODE_PLAY_PAUSE = 1111;
    private static int NOTIFICATION_ID = 123;

    private final IBinder iBinder = new ServiceBinder();
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;

    private int[] rainy = new int[]{R.raw.perfect_storm, R.raw.rain_on_window, R.raw.rain_on_leaves, R.raw.light_rain, R.raw.lake_rain
            , R.raw.rain_on_roof, R.raw.rain_on_sidewalk, R.raw.beach_main, R.raw.peaceful_water, R.raw.tent, R.raw.ocean_rain, R.raw.fp_main
            , R.raw.th_main};

    private List<MediaPlayer> listMediaPlayer;

    private NotificationManager notificationManager;
    private PendingIntent pendingIntentPlayPause, pendingIntentStop;
    private CountDownTimer countDownTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listMediaPlayer = new ArrayList<>();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence CHANNEL_NAME = "com.example.relax.CHANNEL_NAME";
            int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intentPlayPause = new Intent(ACTION_PLAY_PAUSE);
        pendingIntentPlayPause = PendingIntent.getService(getApplicationContext()
                , REQUEST_CODE_PLAY_PAUSE, intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentStop = new Intent(ACTION_STOP);
        pendingIntentStop = PendingIntent.getService(getApplicationContext()
                , REQUEST_CODE_STOP, intentStop, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_PLAY_PAUSE:
                    changePlayPause();
                    break;

                case ACTION_STOP:
                    stopMedia();
                    cancelCountDown();
                    if (callBack != null) {
                        callBack.finishMainActivity();
                    }
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void showNotification() {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_large_colored);
        int playOrPause;
        String contentText;
        if (isMediaPlaying()) {
            playOrPause = R.drawable.ic_notification_pause;
            contentText = getString(R.string.isPlaying);
        } else {
            playOrPause = R.drawable.ic_notification_play;
            contentText = getString(R.string.isPaused);
        }

        Intent intentResult = new Intent(this, MainActivity.class);
        PendingIntent pendingIntentResult = PendingIntent.getActivity(getApplicationContext(), 1, intentResult, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mNotification = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(getResources().getColor(R.color.colorNotification))
                .setShowWhen(false)
                .setLargeIcon(largeIcon)
                .setContentTitle(getString(R.string.rainySound))
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(playOrPause, "pause", pendingIntentPlayPause)
                .addAction(R.drawable.ic_notification_stop, "stop", pendingIntentStop)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1))
                .setContentIntent(pendingIntentResult)
                .setDefaults(Notification.FLAG_NO_CLEAR);
        startForeground(NOTIFICATION_ID, mNotification.build());
    }

    public void playMedia(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, rainy[position]);
        if (MySharedPreference.getDataBolean(getApplicationContext(), MySharedPreference.STATE_MEDIA)) {
            mediaPlayer.start();
        }

        removeRunnable();

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.getCurrentPosition() > (mediaPlayer.getDuration() - 3000)) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(2000);
                    mediaPlayer.start();
                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);

        showNotification();
    }

    public void changePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                callBack.changeIconPlayPause(false);
                pauseListMedia();
                MySharedPreference.putData(getApplicationContext(), MySharedPreference.STATE_MEDIA, false);
            } else {
                mediaPlayer.start();
                callBack.changeIconPlayPause(true);
                playListMedia();
                MySharedPreference.putData(getApplicationContext(), MySharedPreference.STATE_MEDIA, true);

            }
        }
        showNotification();
    }


    private void pauseListMedia() {
        for (int i = 0; i < listMediaPlayer.size(); i++) {
            if (listMediaPlayer.get(i) != null && listMediaPlayer.get(i).isPlaying()) {
                listMediaPlayer.get(i).pause();
            }
        }
    }

    private void playListMedia() {
        for (int i = 0; i < listMediaPlayer.size(); i++) {
            if (listMediaPlayer.get(i) != null && !listMediaPlayer.get(i).isPlaying()) {
                listMediaPlayer.get(i).start();
            }
        }
    }

    public void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        for (int i = 0; i < listMediaPlayer.size(); i++) {
            if (listMediaPlayer.get(i) != null) {
                listMediaPlayer.get(i).stop();
                listMediaPlayer.get(i).release();
            }
        }

        listMediaPlayer.clear();

        if (notificationManager != null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }

        removeRunnable();
        stopForeground(true);
    }

    public void addMediaPlayer(MediaPlayer mediaPlayer) {
        listMediaPlayer.add(mediaPlayer);
        playListMediaPlayer(listMediaPlayer);
    }

    private void playListMediaPlayer(List<MediaPlayer> list) {
        for (int i = 0; i < list.size(); i++) {
            if(mediaPlayer != null && mediaPlayer.isPlaying())
            {
                if (list.get(i) != null && !list.get(i).isPlaying()) {
                    list.get(i).start();
                }
            }
        }
    }

    private boolean isMediaPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

//    private boolean isListMediaPlaying() {
//        boolean check = false;
//        for (int i = 0; i < listMediaPlayer.size(); i++) {
//            if (listMediaPlayer.get(i) != null && listMediaPlayer.get(i).isPlaying()) {
//                check = true;
//            } else {
//                break;
//            }
//        }
//
//        return check;
//    }

    public void removeMediaPlayer(int position) {
        if (listMediaPlayer.get(position) != null && listMediaPlayer.get(position).isPlaying()) {
            listMediaPlayer.get(position).stop();
        }
        listMediaPlayer.remove(position);
        playListMediaPlayer(listMediaPlayer);
    }

    @Override
    public void onDestroy() {
//        removeRunnable();
        super.onDestroy();
    }

    private void removeRunnable() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void countDown(long time) {
        cancelCountDown();
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("cccccccc", "bbbb " + MyUtil.formatTime(millisUntilFinished));

            }

            @Override
            public void onFinish() {
                stopMedia();
                if (callBack != null) {
                    callBack.finishMainActivity();
                }
            }
        };
        countDownTimer.start();
    }

    private void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public interface CallBack {
        void changeIconPlayPause(boolean b);

        void finishMainActivity();
    }

    public class ServiceBinder extends Binder {

        public RainySoundService getService() {
            return RainySoundService.this;
        }
    }
}
