package com.zplay.playable.zplayvideoplayer;

import android.graphics.RectF;
import android.util.Log;

import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_CLICK;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_SWEEP_DOWN;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_SWEEP_LEFT;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_SWEEP_RIGHT;
import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_SWEEP_UP;
import static java.lang.Math.abs;

class Monitoring {
    static final int INVALID = -1;
    static final int PAUSE = 0;

    private float videoDuration;
    private CtrlAction[] ctrls;

    Monitoring(float videoDuration, CtrlAction[] ctrls) {
        this.videoDuration = videoDuration;
        this.ctrls = ctrls;
    }

    int nextPoint(int currentPoint) {
        float current = currentPoint * 0.001f / videoDuration;
        if (ctrls == null || ctrls.length == 0) {
            return INVALID;
        }

        for (CtrlAction ca : ctrls) {
            if (ca.passed) {
                continue;
            }

            if (ca.span.end < current) {
                if (ca.span.isLoop()) {
                    return (int) (ca.span.loopStart * videoDuration * 1000);
                } else {
                    return PAUSE;
                }
            }
        }
        return INVALID;
    }

    int performAction(int currentPoint, float x, float y, int gesture) {
        float normalizedTime = currentPoint * 0.001f / videoDuration;
        if (ctrls == null || ctrls.length == 0) {
            return INVALID;
        }

        for (CtrlAction ca : ctrls) {
            if (ca.passed) {
                continue;
            }

            if (ca.span.contains(normalizedTime)) {
                for (Event e : ca.events) {
                    if (e.getGesture() == gesture && e.contains(x, y)) {
                        ca.passed = true;
                        Log.d("paCCC", "performAction: " + ca.span.end + ":"+videoDuration+" = " + (int) (ca.span.end * videoDuration * 1000));
                        return (int) (ca.span.end * videoDuration * 1000);
                    }
                }
            }
        }
        return INVALID;
    }

    static boolean floatEquals(float f1, float f2) {
        return abs(f1 - f2) < 0.00001;
    }

    static class CtrlAction {
        int id;
        Span span;
        Event[] events;
        boolean passed;

        CtrlAction(int id, Span span, Event[] events) {
            this.id = id;
            this.span = span;
            this.events = events;
        }
    }

    static class Span {
        float start;
        float loopStart;
        float end;

        Span(float start, float loopStart, float end) {
            this.start = start;
            this.loopStart = loopStart;
            this.end = end;
        }

        boolean isLoop() {
            return !floatEquals(start, loopStart);
        }

        boolean contains(float normalizedTime) {
            return start <= normalizedTime && normalizedTime < end;
        }
    }

    static class Event {
        float[] block;
        int[] action;
        RectF rectF;

        int getGesture() {
            if (action == null || action.length < 9) {
                return INVALID;
            }

            if (action[0] != 0) {
                return INVALID;
            } else if (action[1] != 0) {
                return GESTURE_SWEEP_UP;
            } else if (action[2] != 0) {
                return INVALID;
            } else if (action[3] != 0) {
                return GESTURE_SWEEP_LEFT;
            } else if (action[4] != 0) {
                return GESTURE_CLICK;
            } else if (action[5] != 0) {
                return GESTURE_SWEEP_RIGHT;
            } else if (action[6] != 0) {
                return INVALID;
            } else if (action[7] != 0) {
                return GESTURE_SWEEP_DOWN;
            } else if (action[8] != 0) {
                return INVALID;
            }
            return INVALID;
        }

        boolean contains(float normalizedX, float normalizedY) {
            if (rectF == null && block != null && block.length == 4) {
                rectF = new RectF(block[0], block[1], block[2], block[3]);
            }


            return rectF != null && rectF.contains(normalizedX, normalizedY);
        }
    }
}
