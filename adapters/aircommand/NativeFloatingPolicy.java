package org.evox.spen.compat;
import android.content.Context;import android.os.Handler;import android.os.Looper;import android.util.Log;import java.lang.ref.WeakReference;
/** Hardware-tool transitions use the original floating-icon controller and settings. */
public final class NativeFloatingPolicy {
 private static WeakReference<Object> service=new WeakReference<>(null);
 private static final Handler main=new Handler(Looper.getMainLooper());
 private static boolean docked;private static boolean previousTouch;
 public static void bind(Object owner){service=new WeakReference<>(owner);if(docked)main.post(()->visible(false,"docked at service startup"));}
 public static void unbind(Object owner){if(service.get()==owner)service.clear();}
 private static boolean multiple(Context c){return SystemSettings.getInt(c.getContentResolver(),"pen_detect_mode_disabled",1)==0;}
 public static void insertion(Context c,boolean inserted){docked=inserted;if(inserted)main.postDelayed(()->visible(false,"pen docked"),250);}
 public static void penFrame(Context c,boolean touch){boolean down=touch&&!previousTouch;previousTouch=touch;if(down&&multiple(c))main.post(()->visible(true,"multiple S Pen contact"));}
 public static void fingerDown(Context c){if(multiple(c))main.post(()->visible(false,"finger contact"));}
 private static void visible(boolean show,String reason){
  Object owner=service.get();if(owner==null)return;
  try{Object ui=owner.getClass().getField("l").get(owner);if(ui==null)return;
   Object floating=ui.getClass().getField("h").get(ui);
   if(show)floating.getClass().getMethod("n").invoke(floating);
   floating.getClass().getMethod("d",boolean.class,boolean.class,Runnable.class).invoke(floating,!show,false,null);
   Log.i("AirCommandPort","Original floating icon "+(show?"shown":"hidden")+": "+reason+", docked="+docked);
  }catch(ReflectiveOperationException e){Log.e("AirCommandPort","Original floating controller failed",e);}
 }
}
