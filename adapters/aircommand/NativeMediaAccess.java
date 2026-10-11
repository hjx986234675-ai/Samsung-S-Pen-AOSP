package org.evox.spen.compat;
import android.content.*;import android.os.*;import android.media.session.*;import android.service.notification.*;import java.util.*;import java.util.concurrent.CopyOnWriteArrayList;
/** Public media-session API feeding Samsung's existing monitor and rule selection. */
public final class NativeMediaAccess {
 private static java.lang.ref.WeakReference<NotificationListenerService> connected=new java.lang.ref.WeakReference<>(null);
 private static final String SERVICE="com.samsung.android.service.aircommand.domain.airaction.monitor.NotificationServiceMonitor";
 public static void initialize(Object monitor,int user){try{
  if(user!=UserAccess.myId())throw new SecurityException("Media monitoring limited to current user");
  NotificationListenerService live=connected.get();if(live!=null){monitor.getClass().getField("d").set(monitor,live);if(monitor.getClass().getField("g").get(monitor)!=null)live.getClass().getField("h").set(live,monitor.getClass().getField("i").get(monitor));}
  Context c=(Context)monitor.getClass().getField("a").get(monitor);MediaSessionManager manager=(MediaSessionManager)monitor.getClass().getField("c").get(monitor);
  MediaSessionManager.OnActiveSessionsChangedListener callback=(MediaSessionManager.OnActiveSessionsChangedListener)monitor.getClass().getField("j").get(monitor);
  ComponentName component=new ComponentName(c,SERVICE);manager.removeOnActiveSessionsChangedListener(callback);
  manager.addOnActiveSessionsChangedListener(callback,component,(Handler)monitor.getClass().getField("b").get(monitor));
  List<MediaController> sessions=manager.getActiveSessions(component);notifications(monitor,sessions);callback.onActiveSessionsChanged(sessions);
  android.util.Log.i("AirCommandPort","Original media monitor registered using public Android API; sessions="+sessions.size());
 }catch(SecurityException denied){android.util.Log.w("AirCommandPort","Automatic media control requires Air Command notification access");}catch(ReflectiveOperationException error){throw new IllegalStateException("Original media monitor layout changed",error);}}
 public static void attach(NotificationListenerService service){RuntimeContext.init(service);connected=new java.lang.ref.WeakReference<>(service);try{Class<?> type=Class.forName("l2.h",false,service.getClassLoader());Object monitor=type.getMethod("b",Context.class).invoke(null,service);type.getField("d").set(monitor,service);if(type.getField("g").get(monitor)!=null)service.getClass().getField("h").set(service,type.getField("i").get(monitor));initialize(monitor,UserAccess.myId());}catch(ReflectiveOperationException error){throw new IllegalStateException("Original notification monitor layout changed",error);}}
 public static void detach(NotificationListenerService service){if(connected.get()==service)connected.clear();}
 public static void notifications(Object monitor,List<MediaController> sessions){try{Object service=monitor.getClass().getField("d").get(monitor);if(!(service instanceof NotificationListenerService))return;StatusBarNotification[] notifications=((NotificationListenerService)service).getActiveNotifications();if(notifications==null)return;CopyOnWriteArrayList<String> packages=new CopyOnWriteArrayList<>();for(MediaController controller:sessions)for(StatusBarNotification notification:notifications)if(controller.getPackageName().equals(notification.getPackageName()))packages.addIfAbsent(controller.getPackageName());CopyOnWriteArrayList<String> existing=(CopyOnWriteArrayList<String>)monitor.getClass().getField("e").get(monitor);existing.clear();existing.addAll(packages);}catch(SecurityException unavailable){android.util.Log.w("AirCommandPort","Media notification listener not yet connected");}catch(ReflectiveOperationException error){throw new IllegalStateException(error);}}

 private static MediaController target(Context context){
  MediaSessionManager manager=(MediaSessionManager)context.getSystemService(Context.MEDIA_SESSION_SERVICE);
  try{List<MediaController> sessions=manager.getActiveSessions(new ComponentName(context,SERVICE));MediaController paused=null;
   for(MediaController controller:sessions){PlaybackState state=controller.getPlaybackState();if(state==null)continue;int value=state.getState();if(value==PlaybackState.STATE_PLAYING||value==PlaybackState.STATE_BUFFERING||value==PlaybackState.STATE_CONNECTING)return controller;if(value==PlaybackState.STATE_PAUSED&&paused==null)paused=controller;}
   return paused;
  }catch(SecurityException denied){android.util.Log.w("AirCommandPort","Media session selection requires notification access");return null;}
 }
 public static boolean supportsAutomatic(Context context,String foreground){
  MediaController controller=target(context);boolean available=controller!=null;
  if(available)android.util.Log.i("AirCommandPort","Original automatic media rule available: target="+controller.getPackageName()+", foreground="+foreground);
  return available;
 }
 public static boolean dispatch(Context context,int key){
  MediaController controller=target(context);if(controller==null)return false;
  android.view.KeyEvent down=new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN,key);
  android.view.KeyEvent up=new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP,key);
  boolean result;
  if(key==android.view.KeyEvent.KEYCODE_VOLUME_UP||key==android.view.KeyEvent.KEYCODE_VOLUME_DOWN){controller.adjustVolume(key==android.view.KeyEvent.KEYCODE_VOLUME_UP?android.media.AudioManager.ADJUST_RAISE:android.media.AudioManager.ADJUST_LOWER,android.media.AudioManager.FLAG_SHOW_UI);result=true;}
  else{boolean first=controller.dispatchMediaButtonEvent(down);boolean second=controller.dispatchMediaButtonEvent(up);result=first&&second;}
  android.util.Log.i("AirCommandPort","Original media action dispatched: target="+controller.getPackageName()+", key="+key+", accepted="+result);
  return result;
 }
}
