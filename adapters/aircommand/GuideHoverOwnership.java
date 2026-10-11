package org.evox.spen.compat;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import java.lang.ref.WeakReference;

/** Keep window-routing exits separate from physical pen departure. */
public final class GuideHoverOwnership {
    private static final Handler main = new Handler(Looper.getMainLooper());
    private static WeakReference<View> anchor = new WeakReference<>(null);
    private static WeakReference<View.OnHoverListener> owner = new WeakReference<>(null);
    private static final Rect bounds = new Rect();
    private static boolean active, replaying;
    private static volatile boolean hardwareKnown, hovering;
    private static volatile int rawX, rawY;
    private static Context context;
    private static long outsideSince;

    public static void frame(Context c, boolean hover, boolean touch, int x, int y) {
        context = c; rawX = x; rawY = y;
        hovering = hover && !touch && x >= 0 && y >= 0;
        hardwareKnown = true;
    }

    public static void reset() { hovering = false; hardwareKnown = true; }

    public static boolean intercept(View view, MotionEvent event, View.OnHoverListener listener) {
        if (replaying || !hardwareKnown) return false;
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_HOVER_ENTER) {
            if (active && anchor.get() == view) return true;
            anchor = new WeakReference<>(view);
            owner = new WeakReference<>(listener);
            View root = view.getRootView();
            int[] position = new int[2]; root.getLocationOnScreen(position);
            bounds.set(position[0], position[1], position[0] + root.getWidth(),
                position[1] + root.getHeight());
            // Freeze the floating window bounds across Samsung's icon animation.
            active = true; outsideSince = 0;
            main.removeCallbacks(check); main.postDelayed(check, 50);
            GuidePanelInputRegion.rememberAnchor(view, event);
            Log.i("AirCommandPort", "Guide physical hover started bounds=" + bounds);
        } else if (action == MotionEvent.ACTION_HOVER_EXIT && active && anchor.get() == view) {
            // A new overlay can synthesize EXIT even though the pen has not moved.
            // The hardware reader below decides when to deliver the real exit.
            return true;
        }
        return false;
    }

    private static boolean inside() {
        if (!hardwareKnown) return false;
        if (!hovering || context == null) return false;
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point(); display.getRealSize(size);
        float u = Math.max(0, Math.min(1, rawX / 16714f));
        float v = Math.max(0, Math.min(1, rawY / 26712f));
        float x = u, y = v;
        switch (display.getRotation()) {
            case 1: x = v; y = 1 - u; break;
            case 2: x = 1 - u; y = 1 - v; break;
            case 3: x = 1 - v; y = u; break;
        }
        return bounds.contains(Math.round(x * (size.x - 1)), Math.round(y * (size.y - 1)));
    }

    private static final Runnable check = new Runnable() {
        public void run() {
            if (!active) return;
            View view = anchor.get(); View.OnHoverListener listener = owner.get();
            if (view == null || listener == null || !view.isAttachedToWindow()) {
                active = false; return;
            }
            long now = SystemClock.uptimeMillis();
            if (inside()) outsideSince = 0;
            else if (outsideSince == 0) outsideSince = now;
            else if (now - outsideSince >= 120) {
                active = false;
                MotionEvent exit = MotionEvent.obtain(now, now, MotionEvent.ACTION_HOVER_EXIT, 0, 0, 0);
                replaying = true;
                try { listener.onHover(view, exit); }
                finally { replaying = false; exit.recycle(); }
                Log.i("AirCommandPort", "Guide physical hover ended");
                return;
            }
            main.postDelayed(this, 50);
        }
    };
}
