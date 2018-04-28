package com.zplay.playable.zplayvideoplayer;

import android.graphics.RectF;

import static com.zplay.playable.zplayvideoplayer.ZGesture.GESTURE_CLICK;
import static java.lang.Math.abs;

public class Monitoring {
    final int screenOrientation;
    final float videoDuration;
    CtrlAction[] ctrls;

    Monitoring() {
        screenOrientation = 0;
        videoDuration = 31.2f * 1000;
        ctrls = new CtrlAction[3];

        Event[] e0 = new Event[1];
        e0[0] = new Event(new Block(0f, 0f, 1f, 0.4625832093828485f), GESTURE_CLICK);
        ctrls[0] = new CtrlAction(0, new Span(0.056572788065843614f, 0.1337448559670782f, 0.22016460905349794f), e0);

        Event[] e1 = new Event[1];
        e1[0] = new Event(new Block(0f, 0.5103021140669577f, 1f, 1f), GESTURE_CLICK);
        ctrls[1] = new CtrlAction(1, new Span(0.2561612654320988f, 0.31584362139917693f, 0.4495884773662551f), e1);

        Event[] e2 = new Event[1];
        e2[0] = new Event(new Block(0f, 0f, 1f, 0.47350710784911365f), GESTURE_CLICK);
        ctrls[1] = new CtrlAction(1, new Span(0.5720048868312757f, 0.6368312757201646f, 0.8930041152263375f), e2);
    }

    int nextSeek(int currentPoint, float downX, float downY, int gesture) {
        final int invalid = -1;
        int ctrlIndex = getCtrlsIndex(currentPoint);
        if (ctrlIndex == invalid) {
            return invalid;
        }

        CtrlAction ca = ctrls[ctrlIndex];
        for (int i = 0; i < ca.events.length; i++) {
            Event e = ca.events[i];
            if (e.block.contains(downX, downY) && e.gesture == gesture) {
                ca.passed = true;
                return (int) ca.span.end;
            }
        }
        return invalid;
    }

    static boolean floatEquals(float f1, float f2) {
        return abs(f1 - f2) < 0.00001;
    }

    private int getCtrlsIndex(int currentPoint) {
        int index = -1;
        if (ctrls == null) {
            return index;
        }
        for (int i = 0; i < ctrls.length; i++) {
            CtrlAction ca = ctrls[i];
            if (ca.passed){
                continue;
            }
            if (ca.span.start <= currentPoint && currentPoint < ca.span.end) {
                return i;
            }
        }
        return index;
    }

    static class CtrlAction {
        final int id;
        final Span span;
        final Event[] events;
        boolean passed;

        CtrlAction(int id, Span span, Event[] events) {
            this.id = id;
            this.span = span;
            this.events = events;
        }
    }

    static class Span {
        final float start;
        final float loopStart;
        final float end;

        Span(float start, float loopStart, float end) {
            this.start = start;
            this.loopStart = loopStart;
            this.end = end;
        }

        boolean isLoop() {
            return floatEquals(start, loopStart);
        }
    }

    static class Event {
        final Block block;
        final int gesture;

        Event(Block block, int gesture) {
            this.block = block;
            this.gesture = gesture;
        }
    }

    static class Block {
        final RectF rectF;

        Block(float l, float t, float r, float b) {
            rectF = new RectF();
            rectF.set(l, t, r, b);
        }

        boolean contains(float x, float y) {
            return rectF.contains(x, y);
        }
    }
}
