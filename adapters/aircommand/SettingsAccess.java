package org.evox.spen.compat;
import android.content.ContentResolver;
import android.provider.Settings;
public final class SettingsAccess {
 public static int getSystemIntForUser(ContentResolver r,String key,int def,int user) {
  if(key.startsWith("pen_")||key.startsWith("spen_")||key.startsWith("air_command")||key.startsWith("aircommand")||key.equals("screen_off_memo")) return SystemSettings.getInt(r,key,def);
  try {return (Integer)Settings.System.class.getMethod("getIntForUser", ContentResolver.class,String.class,int.class,int.class).invoke(null,r,key,def,user);}
  catch(ReflectiveOperationException e) {
   int own=android.os.Process.myUid()/100000;
   if(user>=0 && user!=own) return def;
   return SystemSettings.getInt(r,key,def);
  }
 }
}
