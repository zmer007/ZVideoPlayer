package com.zplay.playable.zplayvideoplayer;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/4/27.
 */

public class PlayableActivity extends Activity implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener,
        TextureView.SurfaceTextureListener {
    private static final String VIDEO_PATH = Environment.getExternalStorageDirectory() + File.separator + "littlefox.mp4";
    private static final String TAG = "PlayableActivity";

    private MediaPlayer mediaPlayer;
    private Surface surface;

    // 从服务器获得视频宽高
    private static final int VIDEO_WIDTH = 852;
    private static final int VIDEO_HEIGHT = 480;

    private Monitoring monitoring;
    private TimeHandler timeHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout rootView = new FrameLayout(this);
        rootView.setBackgroundColor(0xffffffff);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        VideoFace videoFace = new VideoFace(this, new VideoFace.VideoInfo(VIDEO_WIDTH, VIDEO_HEIGHT));
        rootView.addView(videoFace);
        GestureDetectorView gestureDetectorView = new GestureDetectorView(this, videoFace.getFrameRect());
        gestureDetectorView.setGestureListener(mGestureListener);
        rootView.addView(gestureDetectorView);
        setContentView(rootView);

        videoFace.setSurfaceTextureListener(this);
        mediaPlayer = new MediaPlayer();

        monitoring = MonitoringParser.fromeJson();
        timeHandler = new TimeHandler(this);
    }

    private GestureDetectorView.ZGestureListener mGestureListener = new GestureDetectorView.ZGestureListener() {
        @Override
        public void onSweepLeft(float startX, float startY) {
            performAction(startX, startY, ZGesture.GESTURE_SWEEP_LEFT);
        }

        @Override
        public void onSweepRight(float startX, float startY) {
            performAction(startX, startY, ZGesture.GESTURE_SWEEP_RIGHT);
        }

        @Override
        public void onSweepUp(float startX, float startY) {
            performAction(startX, startY, ZGesture.GESTURE_SWEEP_UP);
        }

        @Override
        public void onSweepDown(float startX, float startY) {
            performAction(startX, startY, ZGesture.GESTURE_SWEEP_DOWN);
        }

        @Override
        public void onClick(float startX, float startY) {
            performAction(startX, startY, ZGesture.GESTURE_CLICK);
        }

        @Override
        public void onClick(float startX, float startY, float times) {
            performAction(startX, startY, ZGesture.GESTURE_CLICKS);
        }
    };

    private void performAction(float normalizedX, float normalizedY, int gesture) {
        int nextPoint = monitoring.performAction(timeHandler.getCurrentPoint(), normalizedX, normalizedY, gesture);
        if (nextPoint != Monitoring.INVALID) {
            timeHandler.refreshStartTime(nextPoint);
            Log.d(TAG, "performAction: seek " + nextPoint);
            mediaPlayer.seekTo(nextPoint);
        }
    }

    private void loadMedia(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setSurface(surface);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.w(TAG, "Media load failed");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Log.w(TAG, "Media prepare failed");
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable: ");
        this.surface = new Surface(surface);
        loadMedia(VIDEO_PATH);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged: ");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed: ");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared: ");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
        timeHandler.stopClick();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError: ");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.d(TAG, "onInfo: MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                Log.d(TAG, "onInfo: MEDIA_INFO_BUFFERING_START");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                Log.d(TAG, "onInfo: MEDIA_INFO_BUFFERING_END");
                break;
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.d(TAG, "onInfo: MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.d(TAG, "onInfo: MEDIA_INFO_NOT_SEEKABLE");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Log.d(TAG, "onInfo: MEDIA_INFO_METADATA_UPDATE");
                break;
            case MediaPlayer.MEDIA_INFO_UNKNOWN:
                Log.d(TAG, "onInfo: MEDIA_INFO_UNKNOWN");
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mediaPlayer.start();
            timeHandler.refreshStartTime(mediaPlayer.getCurrentPosition());
            timeHandler.startClick();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeHandler.stopClick();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    void onMediaPlayerProgress(int currentPoint) {
        Log.d(TAG, "onMediaPlayerProgress: ");
        int nextStep = monitoring.nextPoint(currentPoint);
        if (nextStep == Monitoring.INVALID) {
            return;
        }

        if (nextStep == Monitoring.PAUSE) {
            timeHandler.stopClick();
            mediaPlayer.pause();
            return;
        }

        mediaPlayer.seekTo(nextStep);
        timeHandler.refreshStartTime(nextStep);
        Log.d(TAG, "onMediaPlayerProgress: loop");
    }


    static class TimeHandler extends Handler {

        private WeakReference<PlayableActivity> paRef;
        long startTime;
        boolean stopNextExe;

        TimeHandler(PlayableActivity pa) {
            paRef = new WeakReference<>(pa);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PlayableActivity pa = paRef.get();
            if (pa == null || stopNextExe) {
                return;
            }

            pa.onMediaPlayerProgress((int) (System.currentTimeMillis() - startTime));
            sendEmptyMessageDelayed(0, 16);
        }

        void refreshStartTime(int currentPoint) {
            startTime = System.currentTimeMillis() - currentPoint;
        }

        void startClick() {
            stopNextExe = false;
            sendEmptyMessage(0);
        }

        void stopClick() {
            stopNextExe = true;
        }

        int getCurrentPoint() {
            return (int) (System.currentTimeMillis() - startTime);
        }
    }

}
