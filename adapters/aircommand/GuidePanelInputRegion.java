package org.evox.spen.compat;

import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;

/** Prevents the guide overlay from taking hover away from its own floating icon. */
public final class GuidePanelInputRegion {
    private static WeakReference<View> anchor = new WeakReference<>(null);
    private static final int LISTENER_TAG = 0x7e0f0001;

    public static void rememberAnchor(View view, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_HOVER_ENTER) {
            anchor = new WeakReference<>(view);
        }
    }

    public static void install(final View panel) {
        try {
            final ViewTreeObserver observer = panel.getViewTreeObserver();
            final Class<?> listenerClass = Class.forName(
                "android.view.ViewTreeObserver$OnComputeInternalInsetsListener");
            // Resolve protected members before registering a callback, so unsupported
            // application policy cannot throw through ViewTreeObserver's traversal.
            Class<?> infoClass = Class.forName("android.view.ViewTreeObserver$InternalInsetsInfo");
            final java.lang.reflect.Field contentField = infoClass.getField("contentInsets");
            final java.lang.reflect.Field visibleField = infoClass.getField("visibleInsets");
            final java.lang.reflect.Field regionField = infoClass.getField("touchableRegion");
            final java.lang.reflect.Method setInsets = infoClass.getMethod("setTouchableInsets", int.class);
            Object previous = panel.getTag(LISTENER_TAG);
            if (previous != null) {
                observer.getClass().getMethod("removeOnComputeInternalInsetsListener", listenerClass)
                    .invoke(observer, previous);
            }
            Object listener = Proxy.newProxyInstance(listenerClass.getClassLoader(),
                new Class<?>[]{listenerClass}, (proxy, method, args) -> {
                    if (method.getName().equals("equals")) return proxy == args[0];
                    if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
                    if (method.getName().equals("toString")) return "S Pen guide input region";
                    if (!method.getName().equals("onComputeInternalInsets")) return null;
                    try {
                    Object info = args[0];
                    ((Rect)contentField.get(info)).setEmpty();
                    ((Rect)visibleField.get(info)).setEmpty();
                    Region region = (Region)regionField.get(info);
                    region.set(0, 0, panel.getWidth(), panel.getHeight());
                    View icon = anchor.get();
                    if (icon != null && icon.isAttachedToWindow()) {
                        int[] iconPosition = new int[2];
                        int[] panelPosition = new int[2];
                        icon.getLocationOnScreen(iconPosition);
                        panel.getLocationOnScreen(panelPosition);
                        int x = iconPosition[0] - panelPosition[0];
                        int y = iconPosition[1] - panelPosition[1];
                        region.op(new Rect(x, y, x + icon.getWidth(), y + icon.getHeight()),
                            Region.Op.DIFFERENCE);
                    }
                    setInsets.invoke(info, 3);
                    } catch (ReflectiveOperationException | RuntimeException failure) {
                        Log.e("AirCommandPort", "Guide input region callback failed", failure);
                    }
                    return null;
                });
            observer.getClass().getMethod("addOnComputeInternalInsetsListener", listenerClass)
                .invoke(observer, listener);
            panel.setTag(LISTENER_TAG, listener);
            Log.i("AirCommandPort", "Guide input region installed; floating icon excluded");
        } catch (ReflectiveOperationException | RuntimeException failure) {
            Log.e("AirCommandPort", "Guide input region could not be installed", failure);
        }
    }
}
