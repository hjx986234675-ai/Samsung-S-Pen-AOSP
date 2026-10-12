package org.evox.spen.compat;
import android.content.*;import android.os.*;
public final class ServiceAccess {
 public static void inspect(Context c){
  for(String name:new String[]{"com.samsung.android.view.SemWindowManager$FoldStateListener","e4.c","g4.k"}) {
   try {Class<?> found=Class.forName(name,false,c.getClassLoader());android.util.Log.i("AirCommandPort","Class check: "+name+" loader="+found.getClassLoader());}
   catch(Throwable e){android.util.Log.e("AirCommandPort","Class check failed: "+name,e);}
  }
 }

 public static ComponentName start(Context c,Intent intent,UserHandle user) {
  if(UnlockPolicy.disabled(intent))return null;
  if(intent.getComponent()!=null && "com.samsung.android.app.notes.screenoffmemo.ScreenOffMemoService".equals(intent.getComponent().getClassName()))return null;
  RuntimeContext.init(c);
  Parcel p=Parcel.obtain();int id;
  try{user.writeToParcel(p,0);p.setDataPosition(0);id=p.readInt();}finally{p.recycle();}
  if(id>=0 && id!=UserAccess.myId())throw new SecurityException("Cross-user service start unsupported in app port");
  if(intent.getComponent()!=null && "com.samsung.android.app.smartcapture".equals(intent.getComponent().getPackageName())){
   Intent original=new Intent(intent);String name=original.getComponent().getClassName();
   if("com.samsung.android.app.smartselect.SmartClipMainService".equals(name))original.setClassName("com.samsung.android.app.smartcapture","com.samsung.android.app.smartcapture.smartselect.SmartClipMainService");
   Intent entry=new Intent().setClassName("com.samsung.android.app.smartcapture","org.evox.spen.capture.CaptureEntryActivity").putExtra("original_service",original).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   try{c.startActivity(entry);return original.getComponent();}catch(RuntimeException error){android.util.Log.e("AirCommandPort","Original capture entry unavailable",error);new Handler(Looper.getMainLooper()).post(()->android.widget.Toast.makeText(c,"This tool is currently unavailable",android.widget.Toast.LENGTH_SHORT).show());return null;}
  }
  try{return c.startService(intent);}catch(IllegalStateException blocked){
   if(!"com.samsung.android.service.aircommand.action.REMOTE_SPEN_SERVICE".equals(intent.getAction())){
    android.util.Log.e("AirCommandPort","Original tool launch blocked by Android service policy: "+intent.getComponent(),blocked);
    new Handler(Looper.getMainLooper()).post(()->android.widget.Toast.makeText(c,"This tool is currently unavailable",android.widget.Toast.LENGTH_SHORT).show());
    return null;
   }
   Context app=c.getApplicationContext();Intent nativeIntent=new Intent(intent);new Handler(Looper.getMainLooper()).postDelayed(()->{try{app.startService(nativeIntent);android.util.Log.i("AirCommandPort","Original RemoteSpenService started after activity initialization");}catch(RuntimeException error){android.util.Log.w("AirCommandPort","Original BLE service start: "+error.getMessage());}},1000);return null;
  }
 }
}
