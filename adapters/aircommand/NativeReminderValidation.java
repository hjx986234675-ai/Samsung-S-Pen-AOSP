package org.evox.spen.compat;
import android.app.Service;import android.app.Dialog;import android.content.Intent;import android.os.Handler;import android.os.Looper;import android.util.Log;
/** Fixed software UI probe; never claims a physical walking test. */
public final class NativeReminderValidation {
 public static boolean intent(Service service,Intent intent){
  if(intent==null||!"evox_reminder_probe".equals(intent.getStringExtra("cause")))return false;
  try{
   Object controller=service.getClass().getField("h").get(service);
   NativeStepAlert manager=(NativeStepAlert)controller.getClass().getField("v").get(controller);
   Dialog dialog=(Dialog)controller.getClass().getMethod("f").invoke(controller);
   dialog.show();
   new Handler(Looper.getMainLooper()).postDelayed(()->{
    Log.i("AirCommandPort","Original reminder software probe: sensor_available="+(manager!=null&&manager.isAvailableService(3))+", dialog_attached="+dialog.getWindow().getDecorView().isAttachedToWindow()+", window_type="+dialog.getWindow().getAttributes().type);
    dialog.dismiss();service.stopSelf();
   },400);
  }catch(ReflectiveOperationException|RuntimeException|LinkageError e){Log.e("AirCommandPort","Original reminder software probe failed",e);service.stopSelf();}
  return true;
 }
}
