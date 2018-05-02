package com.zplay.playable.zplayvideoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_CLICK;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_NONE;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_SWEEP;
import static java.lang.Math.abs;

/**
 * 检测自定义手势视图
 */
public final class GestureDetectorView extends View {
    private static final String TAG = "GestureTextureView";

    private static final float DEFAULT_SWEEP_DELTA = 10;

    private ZGestureListener mGestureListener;
    private float mDownX;
    private float mDownY;
    private long mDownTime;
    private long mEndTime;

    private int width;
    private int height;

    private float mValidSweepDelta;

    private int mLastGesture = GESTURE_NONE;

    private GestureDetectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private GestureDetectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GestureDetectorView(Context context, RectF dstRect) {
        super(context);
        mValidSweepDelta = DEFAULT_SWEEP_DELTA * getResources().getDisplayMetrics().density;

        width = (int) dstRect.width();
        height = (int) dstRect.height();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.topMargin = (int) dstRect.top;
        params.leftMargin = (int) dstRect.left;
        setBackgroundColor(0x8800ff00);
        setLayoutParams(params);
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return mGestureListener != null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mEndTime = System.currentTimeMillis();
                parseGesture(mDownX, mDownY, event.getX(), event.getY(), System.currentTimeMillis() - mDownTime);
                break;
            case MotionEvent.ACTION_CANCEL:
                mEndTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
        return true;
    }

    private void parseGesture(float downX, float downY, float upX, float upY, long duration) {
        if (isClick(downX, downY, upX, upY, duration)) {
            mLastGesture = GESTURE_CLICK;
            Log.d(TAG, "cccccccccccccccc: ");
            mGestureListener.onClick(normalizedX(downX), normalizedY(downY));
        } else if (isSweep(downX, downY, upX, upY)) {
            mLastGesture = GESTURE_SWEEP;
            performSweep(downX, downY, upX, upY);
        }
    }

    private boolean isClick(float dx, float dy, float ux, float uy, long duration) {
        return dx == ux && dy == uy && duration < 100;
    }

    private boolean isSweep(float dx, float dy, float ux, float uy) {
        return (abs(abs(dx) - abs(ux)) > mValidSweepDelta) || (abs(abs(dy) - abs(uy)) > mValidSweepDelta);
    }

    private void performSweep(float dx, float dy, float ux, float uy) {
        if (abs(abs(dx) - abs(ux)) > abs(abs(dy) - abs(uy))) {
            if (ux - dx > 0) {
                mGestureListener.onSweepRight(normalizedX(dx), normalizedY(dy));
            } else {
                mGestureListener.onSweepLeft(normalizedX(dx), normalizedY(dy));
            }
        } else {
            if (uy - dy > 0) {
                mGestureListener.onSweepDown(normalizedX(dx), normalizedY(dy));
            } else {
                mGestureListener.onSweepUp(normalizedX(dx), normalizedY(dy));
            }
        }
    }

    void setGestureListener(ZGestureListener listener) {
        mGestureListener = listener;
    }

    void setValidSweepDelta(float delta) {
        mValidSweepDelta = delta * getResources().getDisplayMetrics().density;
    }

    float normalizedX(float x) {
        return x / width;
    }

    float normalizedY(float y) {
        return y / height;
    }

    interface ZGestureListener {
        void onSweepLeft(float startX, float startY);

        void onSweepRight(float startX, float startY);

        void onSweepUp(float startX, float startY);

        void onSweepDown(float startX, float startY);

        void onClick(float startX, float startY);

        void onClick(float startX, float startY, float times);
    }
}
