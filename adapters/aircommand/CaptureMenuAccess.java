package org.evox.spen.compat;
import android.content.*;
/** Original Air Command shortcut opens original SmartClip tools through its own package entry. */
public final class CaptureMenuAccess {
 public static void launch(Context c,Intent intent){try{c.startActivity(intent);android.util.Log.i("AirCommandPort","Original SmartClip capture entry requested");}catch(RuntimeException error){android.util.Log.e("AirCommandPort","Original SmartClip entry failed",error);android.widget.Toast.makeText(c,"三星智能选择启动失败",android.widget.Toast.LENGTH_SHORT).show();}}
 public static Intent smartSelect(Intent original){
  Intent service=new Intent(original).setClassName("com.samsung.android.app.smartcapture","com.samsung.android.app.smartcapture.smartselect.SmartClipMainService");
  service.putExtra("displayId",0).putExtra("select_mode","RECTANGLE");
  return new Intent().setClassName("com.samsung.android.app.smartcapture","org.evox.spen.capture.CaptureEntryActivity").putExtra("original_service",service).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 }
}
