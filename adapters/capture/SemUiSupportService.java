package com.samsung.android.app;
import android.app.*;import android.content.*;import android.content.res.Configuration;import android.os.*;import android.view.*;import android.provider.Settings;
/** Android service-window lifecycle adapter; layouts/content remain supplied by original Samsung subclasses. */
public class SemUiSupportService extends Service {
 private OriginalDialog dialog;
 private final class OriginalDialog extends Dialog {
  OriginalDialog(){super(SemUiSupportService.this);}
  public boolean dispatchTouchEvent(MotionEvent event){return SemUiSupportService.this.dispatchTouchEvent(event);}
  public boolean dispatchKeyEvent(KeyEvent event){return SemUiSupportService.this.dispatchKeyEvent(event);}
  boolean originalTouch(MotionEvent event){return super.dispatchTouchEvent(event);}
  boolean originalKey(KeyEvent event){return super.dispatchKeyEvent(event);}
 }
 protected void attachBaseContext(Context base){android.hardware.display.DisplayManager manager=(android.hardware.display.DisplayManager)base.getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);Display display=manager.getDisplay(Display.DEFAULT_DISPLAY);if(display==null)display=manager.getDisplay(Display.DEFAULT_DISPLAY);if(display==null)throw new IllegalStateException("No display available for original UI service");super.attachBaseContext(base.createDisplayContext(display).createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,null));}
 private Dialog dialog(){if(dialog==null){dialog=new OriginalDialog();dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));}return dialog;}
 public Context getContext(){return this;}
 public WindowManager.LayoutParams getWindowAttributes(){return getWindow().getAttributes();}
 public void stopUiSupportService(){stopSelf();}
 public boolean dispatchTouchEvent(MotionEvent event){dialog();return dialog.originalTouch(event);}
 public boolean dispatchKeyEvent(KeyEvent event){dialog();return dialog.originalKey(event);}
 public Window getWindow(){return dialog().getWindow();}
 public WindowManager.LayoutParams createLayoutParams(){WindowManager.LayoutParams p=new WindowManager.LayoutParams();p.copyFrom(getWindow().getAttributes());p.width=WindowManager.LayoutParams.MATCH_PARENT;p.height=WindowManager.LayoutParams.MATCH_PARENT;p.gravity=Gravity.TOP|Gravity.LEFT;p.setFitInsetsTypes(0);p.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;return p;}
 public void setContentView(int id){dialog().setContentView(id);}public void setContentView(View view,ViewGroup.LayoutParams p){dialog().setContentView(view,p);}public View findViewById(int id){return dialog().findViewById(id);}
 public void onCreate(){super.onCreate();org.evox.spen.compat.RuntimeContext.init(this);dialog();}
 public void onAttachedToWindow(){}
 public int onStartCommand(Intent intent,int flags,int startId){if(!Settings.canDrawOverlays(this)){android.util.Log.w("AirCommandPort","Original service overlay permission missing");stopSelf();return START_NOT_STICKY;}try{WindowManager.LayoutParams p=createLayoutParams();p.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;getWindow().setAttributes(p);boolean shown=dialog().isShowing();dialog().show();getWindow().setAttributes(p);getWindow().setLayout(p.width,p.height);if(!shown)onAttachedToWindow();}catch(RuntimeException failure){android.util.Log.e("AirCommandPort","Original service window attach failed",failure);stopSelf();}return START_NOT_STICKY;}
 public IBinder onBind(Intent i){return null;}
 public void onConfigurationChanged(Configuration c){super.onConfigurationChanged(c);}
 public void onDestroy(){if(dialog!=null&&dialog.isShowing())dialog.dismiss();dialog=null;super.onDestroy();}
}
