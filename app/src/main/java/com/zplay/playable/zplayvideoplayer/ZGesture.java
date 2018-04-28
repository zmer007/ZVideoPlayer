package com.zplay.playable.zplayvideoplayer;

public class ZGesture {
    static final int GESTURE_NONE = -1;

    static final int GESTURE_CLICK = 0x00;
    static final int GESTURE_CLICKS = 0x01;
    static final int GESTURE_PRESS = 0x02;
    static final int GESTURE_LONG_PRESS = 0x03;

    static final int GESTURE_SWEEP = 0x10;
    static final int GESTURE_SWEEP_LEFT = 0x11;
    static final int GESTURE_SWEEP_UP = 0x12;
    static final int GESTURE_SWEEP_RIGHT = 0x13;
    static final int GESTURE_SWEEP_DOWN = 0x14;

    final int gesture;

    ZGesture(int gesture) {
        this.gesture = gesture;
    }
}
