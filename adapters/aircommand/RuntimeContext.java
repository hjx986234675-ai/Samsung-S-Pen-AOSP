package org.evox.spen.compat;
import android.app.*;import android.content.*;import android.os.*;import java.lang.ref.WeakReference;
public final class RuntimeContext implements Application.ActivityLifecycleCallbacks {
 private static Context app;private static WeakReference<Activity> active=new WeakReference<>(null);
 public static synchronized void init(Context c){if(app!=null)return;app=c.getApplicationContext();UnlockPolicy.apply(app);if(app instanceof Application)((Application)app).registerActivityLifecycleCallbacks(new RuntimeContext());}
 public static Context app(){if(app==null)throw new IllegalStateException("Original app context not initialized");return app;}
 public static Activity active(){return active.get();}
 public void onActivityResumed(Activity a){active=new WeakReference<>(a);}public void onActivityPaused(Activity a){if(active.get()==a)active.clear();}public void onActivityCreated(Activity a,Bundle b){}public void onActivityStarted(Activity a){}public void onActivityStopped(Activity a){}public void onActivitySaveInstanceState(Activity a,Bundle b){}public void onActivityDestroyed(Activity a){if(active.get()==a)active.clear();}
}
