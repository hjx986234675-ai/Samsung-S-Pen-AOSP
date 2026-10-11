package org.evox.spen.compat;
import android.content.Context;import android.graphics.PixelFormat;import android.os.*;import android.provider.Settings;import android.view.*;import android.util.Log;
/** Reserve the side-button tip gesture before the underlying app receives a DOWN. */
public final class NativePenTapShield {
 private static final Handler main=new Handler(Looper.getMainLooper());
 private static WindowManager windows;private static View shield;private static boolean attached,previousTouch,untilRelease;private static volatile boolean requested;
 public static void frame(Context c,boolean near,boolean touch,boolean side){
  boolean enabled=SystemSettings.getInt(c.getContentResolver(),"air_cmd_with_pen_button",1)!=0;
  if(previousTouch&&!touch&&side)untilRelease=true;
  if(!near||!side)untilRelease=false;
  previousTouch=touch;boolean next=enabled&&near&&side&&!untilRelease;
  if(next==requested)return;requested=next;main.post(()->apply(c));
 }
 public static void reset(){requested=false;previousTouch=untilRelease=false;main.post(()->remove());}
 private static void remove(){if(attached){windows.removeViewImmediate(shield);attached=false;Log.i("AirCommandPort","Menu tip input reservation released");}}
 private static void apply(Context c){try{
  if(!requested){remove();return;}
  if(!Settings.canDrawOverlays(c)){Log.e("AirCommandPort","Menu tip input reservation requires existing overlay permission");return;}
  if(attached)return;
  windows=(WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
  shield=new View(c){@Override public boolean onTouchEvent(MotionEvent event){
   int action=event.getActionMasked();
   if(action==MotionEvent.ACTION_DOWN)Log.i("AirCommandPort","Menu tip DOWN consumed before underlying app; tool="+event.getToolType(0));
   if(action==MotionEvent.ACTION_UP||action==MotionEvent.ACTION_CANCEL){requested=false;main.post(()->remove());}
   return true;
  }};
  shield.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
  WindowManager.LayoutParams lp=new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);
  lp.gravity=Gravity.TOP|Gravity.LEFT;lp.setFitInsetsTypes(0);lp.setTitle("Samsung S Pen menu tip input reservation");
  windows.addView(shield,lp);attached=true;Log.i("AirCommandPort","Menu tip input reservation armed before contact");
 }catch(RuntimeException failure){Log.e("AirCommandPort","Menu tip input reservation failed",failure);}}
}
