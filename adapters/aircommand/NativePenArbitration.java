package org.evox.spen.compat;
import android.os.*;import android.util.Log;import java.lang.ref.WeakReference;
/** Share actual proximity with Samsung's button/gesture detectors. */
public final class NativePenArbitration {
 private static WeakReference<Object> callback=new WeakReference<>(null);private static final Handler main=new Handler(Looper.getMainLooper());
 private static volatile boolean near;private static volatile long released=-1000;private static boolean side;private static boolean publishedNear,publishedSide;private static Object publishedCallback;
 public static void bind(Object listener,int kind){if(kind==27){callback=new WeakReference<>(listener);publishedCallback=null;}}
 public static void frame(boolean proximity,boolean button){if(near&&!proximity)released=SystemClock.uptimeMillis();near=proximity;side=proximity&&button;Object target=callback.get();if(target==null)return;if(target==publishedCallback&&publishedNear==near&&publishedSide==side)return;publishedCallback=target;publishedNear=near;publishedSide=side;final boolean h=near,b=side;main.post(()->{try{target.getClass().getMethod("a0",boolean.class).invoke(target,h);target.getClass().getMethod("x",boolean.class).invoke(target,b);Log.i("AirCommandPort","Original detectors updated: proximity="+h+", side="+b);}catch(ReflectiveOperationException failure){Log.e("AirCommandPort","Original proximity callback failed",failure);}});}
 public static boolean suppressAirAction(){boolean suppress=near||SystemClock.uptimeMillis()-released<400;if(suppress)Log.i("AirCommandPort","Near-screen pen sequence suppressed queued air action");return suppress;}
 public static boolean nativeTap(Bundle extras){return extras!=null&&extras.getBoolean("native_pen_tap",false);}
 public static boolean ignoreButton(Bundle extras){return !nativeTap(extras)&&suppressAirAction();}
}
