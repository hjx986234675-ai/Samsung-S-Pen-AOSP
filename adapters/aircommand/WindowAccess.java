package org.evox.spen.compat;
import android.view.WindowManager;
public final class WindowAccess {
 public static void receiveInsets(WindowManager.LayoutParams p,boolean enabled){
  try{p.getClass().getMethod("setReceiveInsetsIgnoringZOrder",boolean.class).invoke(p,enabled);}
  catch(ReflectiveOperationException e){android.util.Log.d("AirCommandPort","Samsung receive-insets extension unavailable; use standard Android window insets");}
 }

 private static void optional(WindowManager.LayoutParams p,String method,int flags){
  try {p.getClass().getMethod(method,int.class).invoke(p,flags);}
  catch(ReflectiveOperationException e){android.util.Log.d("AirCommandPort","Window extension unavailable: "+method+" flags="+flags);}
 }
 public static void addExtension(WindowManager.LayoutParams p,int f){optional(p,"semAddExtensionFlags",f);}
 public static void clearExtension(WindowManager.LayoutParams p,int f){optional(p,"semClearExtensionFlags",f);}
 public static void addPrivate(WindowManager.LayoutParams p,int f){optional(p,"semAddPrivateFlags",f);}
}
