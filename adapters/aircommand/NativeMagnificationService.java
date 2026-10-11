package org.evox.spen.compat;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.MagnificationConfig;
import android.view.accessibility.AccessibilityEvent;
import android.os.*;
public class NativeMagnificationService extends AccessibilityService {
 static volatile NativeMagnificationService instance;
 final Handler handler=new Handler(Looper.getMainLooper());
 protected void onServiceConnected(){instance=this;}
 public void onAccessibilityEvent(AccessibilityEvent event){}

 public void onInterrupt(){getMagnificationController().resetCurrentMagnification(false);}
 public void onDestroy(){getMagnificationController().resetCurrentMagnification(false);instance=null;super.onDestroy();}
 void update(Bundle b){
  final boolean active=b.getBoolean("active");final float x=b.getFloat("x"),y=b.getFloat("y"),scale=b.getFloat("scale",2);
  handler.post(()->{
   boolean ok;
   if(!active) ok=getMagnificationController().resetCurrentMagnification(false);
   else ok=getMagnificationController().setMagnificationConfig(new MagnificationConfig.Builder().setMode(MagnificationConfig.MAGNIFICATION_MODE_WINDOW).setActivated(true).setScale(Math.max(1,Math.min(8,scale))).setCenterX(x).setCenterY(y).build(),false);
   if(!ok) android.util.Log.w("SpenPort","System rejected magnification request");
  });
 }
}
