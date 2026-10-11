package org.evox.spen.capture;
import android.app.Activity;import android.os.Bundle;import android.content.*;import android.util.Log;
/** Foreground entry for original Samsung service; no replacement editor. */
public final class CaptureEntryActivity extends Activity {
 private boolean started, waitingForStorage;
 private boolean isScreenWrite(){Intent original=getIntent().getParcelableExtra("original_service");return "com.sec.spen.flashannotate.FLASH_ANNOTATE_ANIMATION_SERVICE".equals(getIntent().getAction())||(original!=null && original.getComponent()!=null && original.getComponent().getClassName().endsWith(".ScreenWriterService"));}
 protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(request==41){waitingForStorage=false;if(!android.os.Environment.isExternalStorageManager()){android.widget.Toast.makeText(this,"Screen write needs file access to save screenshots",android.widget.Toast.LENGTH_LONG).show();finish();}}}
 public void onCreate(Bundle state){setTheme(android.R.style.Theme_Translucent_NoTitleBar);super.onCreate(state);}
 protected void onResume(){super.onResume();if(waitingForStorage || started)return;
  if(isScreenWrite() && !android.os.Environment.isExternalStorageManager()){waitingForStorage=true;try{startActivityForResult(new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,android.net.Uri.parse("package:"+getPackageName())),41);}catch(RuntimeException error){Log.e("SamsungCapturePort","Original editor storage authorization unavailable",error);finish();}return;}started=true;
  try {Intent service=getIntent().getParcelableExtra("original_service");
   if(service==null && "com.sec.spen.flashannotate.FLASH_ANNOTATE_ANIMATION_SERVICE".equals(getIntent().getAction()))service=new Intent(getIntent().getAction()).setClassName(getPackageName(),"com.samsung.android.app.smartcapture.screenwriter.capture.ScreenWriterService");
   if(service==null && "org.evox.spen.capture.SMART_SELECT".equals(getIntent().getAction()))service=new Intent().setClassName(getPackageName(),"com.samsung.android.app.smartcapture.smartselect.SmartClipMainService").putExtra("displayId",0).putExtra("select_mode","RECTANGLE");
   ComponentName target=service==null?null:service.getComponent();
   if(target==null||!getPackageName().equals(target.getPackageName())||!(target.getClassName().equals("com.samsung.android.app.smartcapture.screenwriter.capture.ScreenWriterService")||target.getClassName().equals("com.samsung.android.app.smartcapture.smartselect.SmartClipMainService")))throw new SecurityException("Unsupported original capture entry");
   if(target.getClassName().endsWith(".SmartClipMainService")){if(!service.hasExtra("displayId"))service.putExtra("displayId",0);if(!service.hasExtra("select_mode"))service.putExtra("select_mode","RECTANGLE");}
   startService(service);Log.i("SamsungCapturePort","Original capture service launched: "+target.getClassName());
  }catch(RuntimeException e){Log.e("SamsungCapturePort","Original capture launch failed",e);android.widget.Toast.makeText(this,"Unable to open Samsung Capture",android.widget.Toast.LENGTH_SHORT).show();}finally{finish();}
 }
}
