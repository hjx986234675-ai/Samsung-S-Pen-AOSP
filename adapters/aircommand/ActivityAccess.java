package org.evox.spen.compat;
public final class ActivityAccess {
 public static void setProcessImportant(android.app.ActivityManager manager,android.os.IBinder token,int pid,boolean important){
  try{manager.getClass().getMethod("setProcessImportant",android.os.IBinder.class,int.class,boolean.class).invoke(manager,token,pid,important);}
  catch(ReflectiveOperationException e){android.util.Log.i("AirCommandPort","Samsung process pinning unavailable; Android service lifecycle active");}
 }

 public static boolean isResumed(android.app.Activity a){
  try {Object lifecycle=a.getClass().getMethod("getLifecycle").invoke(a);Object state=lifecycle.getClass().getMethod("getCurrentState").invoke(lifecycle);return "RESUMED".equals(state.toString());}
  catch(ReflectiveOperationException e){
   try{return (Boolean)android.app.Activity.class.getMethod("isResumed").invoke(a);}
   catch(ReflectiveOperationException unavailable){return !a.isFinishing()&&!a.isDestroyed()&&a.hasWindowFocus();}
  }
 }
}
