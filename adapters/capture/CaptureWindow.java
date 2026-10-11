package org.evox.spen.capture;
import android.content.Context;import android.view.*;
public final class CaptureWindow {
 public static void setType(Window window,int type){
  if(type>=2000){Context context=window.getContext();while(context instanceof android.content.ContextWrapper && !(context instanceof android.app.Activity)){Context base=((android.content.ContextWrapper)context).getBaseContext();if(base==context)break;context=base;}
   type=context instanceof android.app.Activity?WindowManager.LayoutParams.TYPE_APPLICATION:WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
  }
  window.setType(type);
 }

 public static void add(WindowManager ignored,View view,ViewGroup.LayoutParams raw){
  WindowManager.LayoutParams params=(WindowManager.LayoutParams)raw;params.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;params.token=null;
  Context app=org.evox.spen.compat.RuntimeContext.app();android.view.Display display=((android.hardware.display.DisplayManager)app.getSystemService(Context.DISPLAY_SERVICE)).getDisplay(0);
  Context window=app.createDisplayContext(display).createWindowContext(params.type,null);
  ((WindowManager)window.getSystemService(Context.WINDOW_SERVICE)).addView(view,params);
 }
}
