package org.evox.spen.port;
import android.content.Context;import android.net.Uri;import android.os.Bundle;import android.util.Log;
public final class NativeMagnifier {
 private static boolean warned;
 public static void update(Context c,int action,int x,int y,float factor){
  if(action!=7&&action!=9&&action!=10)return;
  float scale=factor>=.7f?1.5f:factor>=.49f?2f:factor>=.39f?2.5f:3f;
  Bundle b=new Bundle();b.putBoolean("active",action!=10);b.putFloat("x",x);b.putFloat("y",y);b.putFloat("scale",scale);
  try{Bundle out=c.getContentResolver().call(Uri.parse("content://com.samsung.android.service.aircommand.aosp.magnification"),"update",null,b);if(out==null||!out.getBoolean("connected")){if(!warned){Log.e("SpenPort","Enable the Air Command magnification accessibility service");warned=true;}}else warned=false;}
  catch(RuntimeException e){if(!warned){Log.e("SpenPort","Air Command magnification service unavailable",e);warned=true;}}
 }
}
