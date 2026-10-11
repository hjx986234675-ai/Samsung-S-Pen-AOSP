package com.samsung.android.app;
import android.app.*;import android.content.*;import android.content.res.Configuration;import android.os.*;import android.view.*;import android.provider.Settings;
/** Android service-window lifecycle adapter; layouts/content remain supplied by original Samsung subclasses. */
public class SemUiSupportService extends Service {
 private Dialog dialog;
 protected void attachBaseContext(Context base){android.hardware.display.DisplayManager manager=(android.hardware.display.DisplayManager)base.getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);Display display=manager.getDisplay(org.evox.spen.compat.DisplayAccess.focusedId());if(display==null)display=manager.getDisplay(Display.DEFAULT_DISPLAY);if(display==null)throw new IllegalStateException("No display available for original UI service");super.attachBaseContext(base.createDisplayContext(display).createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,null));}
 private Dialog dialog(){if(dialog==null){dialog=new Dialog(this);dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);}return dialog;}
 public Window getWindow(){return dialog().getWindow();}
 public WindowManager.LayoutParams createLayoutParams(){WindowManager.LayoutParams p=new WindowManager.LayoutParams();p.copyFrom(getWindow().getAttributes());p.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;return p;}
 public void setContentView(int id){dialog().setContentView(id);}public void setContentView(View view,ViewGroup.LayoutParams p){dialog().setContentView(view,p);}public View findViewById(int id){return dialog().findViewById(id);}
 public void onCreate(){super.onCreate();org.evox.spen.compat.RuntimeContext.init(this);dialog();}
 public void onAttachedToWindow(){}
 public int onStartCommand(Intent intent,int flags,int startId){if(!Settings.canDrawOverlays(this)){android.util.Log.w("AirCommandPort","Original service overlay permission missing");stopSelf();return START_NOT_STICKY;}try{WindowManager.LayoutParams p=createLayoutParams();p.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;getWindow().setAttributes(p);boolean shown=dialog().isShowing();dialog().show();if(!shown)onAttachedToWindow();}catch(RuntimeException failure){android.util.Log.e("AirCommandPort","Original service window attach failed",failure);stopSelf();}return START_NOT_STICKY;}
 public IBinder onBind(Intent i){return null;}
 public void onConfigurationChanged(Configuration c){super.onConfigurationChanged(c);}
 public void onDestroy(){if(dialog!=null&&dialog.isShowing())dialog.dismiss();dialog=null;super.onDestroy();}
}
