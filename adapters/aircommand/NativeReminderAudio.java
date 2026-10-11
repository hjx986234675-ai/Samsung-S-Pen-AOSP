package org.evox.spen.compat;
import android.content.Context;import android.media.Ringtone;import android.media.RingtoneManager;import android.net.Uri;import java.io.*;
public final class NativeReminderAudio {
 public static Ringtone ringtone(Context c,Uri ignored){
  try{File audio=new File(c.getCacheDir(),"samsung-pen-det-alert.ogg");
   if(!audio.isFile())try(InputStream in=c.getAssets().open("samsung-pen-det-alert.ogg");OutputStream out=new FileOutputStream(audio)){byte[] b=new byte[8192];int n;while((n=in.read(b))!=-1)out.write(b,0,n);}
   return RingtoneManager.getRingtone(c,Uri.fromFile(audio));
  }catch(IOException e){android.util.Log.e("AirCommandPort","Original pen reminder audio unavailable",e);return null;}
 }
}
