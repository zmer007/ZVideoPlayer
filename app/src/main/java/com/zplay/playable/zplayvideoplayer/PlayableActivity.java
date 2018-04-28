package com.zplay.playable.zplayvideoplayer;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import java.io.File;
import java.io.IOException;

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
    private VideoFace videoFace;
    private GestureDetectorView gestureDetectorView;
    private Surface surface;

    // 从服务器获得视频宽高
    private static final int VIDEO_WIDTH = 852;
    private static final int VIDEO_HEIGHT = 480;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout rootView = new FrameLayout(this);
        rootView.setBackgroundColor(0xffffffff);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        videoFace = new VideoFace(this, new VideoFace.VideoInfo(VIDEO_WIDTH, VIDEO_HEIGHT));
        rootView.addView(videoFace);

        gestureDetectorView = new GestureDetectorView(this, videoFace.getFrameRect());
        gestureDetectorView.setGestureListener(mGestureListener);
        rootView.addView(gestureDetectorView);

        setContentView(rootView);

        videoFace.setSurfaceTextureListener(this);
        mediaPlayer = new MediaPlayer();
    }

    private GestureDetectorView.ZGestureListener mGestureListener = new GestureDetectorView.ZGestureListener() {
        @Override
        public void onSweepLeft(float startX, float startY) {
            Log.d(TAG, "onSweepLeft: ");
        }

        @Override
        public void onSweepRight(float startX, float startY) {
            Log.d(TAG, "onSweepRight: ");
        }

        @Override
        public void onSweepUp(float startX, float startY) {
            Log.d(TAG, "onSweepUp: ");
        }

        @Override
        public void onSweepDown(float startX, float startY) {
            Log.d(TAG, "onSweepDown: ");
        }

        @Override
        public void onClick(float startX, float startY) {
            Log.d(TAG, "onClick: ");
        }

        @Override
        public void onClick(float startX, float startY, float times) {
            Log.d(TAG, "onClick: ");
        }
    };

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
}
