package com.zplay.playable.zplayvideoplayer;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout.LayoutParams;

/**
 * 视频展示器：
 * 1. 展示视频画面
 * 2. 旋转角度，以便总是全屏状态展示画面
 * 3. 提供在屏幕（竖屏）中的位置
 */
public final class VideoFace extends TextureView {
    private static final String TAG = "VideoFace";

    private RectF mRect;

    private VideoFace(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private VideoFace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoFace(Context context, VideoInfo rawVideoInfo) {
        super(context);

        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float screenHeight = getResources().getDisplayMetrics().heightPixels;

        VideoInfo fixedInfo = fixVideoInfo(rawVideoInfo, screenWidth, screenHeight);

        mRect = initFrameRect(screenWidth, screenHeight, fixedInfo);

        LayoutParams params = new LayoutParams((int) mRect.width(), (int) mRect.height());
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        setRotation(fixedInfo.rotationDegree());
        Matrix matrix = new Matrix();
        matrix.setRotate(fixedInfo.rotationDegree(), mRect.centerX(), mRect.centerY());
        matrix.mapRect(mRect, mRect);
    }

    private RectF initFrameRect(float screenWidth, float screenHeight, VideoInfo vi) {
        float l = (screenWidth - vi.width) / 2;
        float t = (screenHeight - vi.height) / 2;
        float r = l + vi.width;
        float b = t + vi.height;
        return new RectF(l, t, r, b);
    }

    private VideoInfo fixVideoInfo(VideoInfo rawVideoInfo, float screenWidth, float screenHeight) {
        float ratioW = screenWidth / rawVideoInfo.shortSide();
        float ratioH = screenHeight / rawVideoInfo.longSide();
        float ratio = ratioW > ratioH ? ratioH : ratioW;
        return new VideoInfo((int) (rawVideoInfo.width * ratio), (int) (rawVideoInfo.height * ratio));
    }

    RectF getFrameRect() {
        return mRect;
    }

    static class VideoInfo {
        final int width;
        final int height;

        VideoInfo(int w, int h) {
            width = w;
            height = h;
        }

        int longSide() {
            return width > height ? width : height;
        }

        int shortSide() {
            return width > height ? height : width;
        }

        int rotationDegree() {
            return width > height ? 90 : 0;
        }
    }
}
