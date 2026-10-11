package org.evox.spen.compat;
import android.content.ContentResolver;import android.provider.Settings;import android.net.Uri;
/** Samsung typed Secure settings share the same storage as the original preferences UI. */
public final class SecureSettings {
 private static boolean pen(String k){return k.startsWith("pen_")||k.startsWith("spen_")||k.startsWith("air_cmd_")||k.startsWith("aircommand")||k.startsWith("air_command")||k.equals("screen_off_memo");}
 public static String getString(ContentResolver r,String k){return pen(k)?SystemSettings.getString(r,k):Settings.Secure.getString(r,k);}
 public static int getInt(ContentResolver r,String k,int d){if(!pen(k))return Settings.Secure.getInt(r,k,d);return SystemSettings.getInt(r,k,d);}
 public static int getInt(ContentResolver r,String k)throws Settings.SettingNotFoundException {return pen(k)?SystemSettings.getInt(r,k):Settings.Secure.getInt(r,k);}
 public static long getLong(ContentResolver r,String k,long d){return pen(k)?SystemSettings.getLong(r,k,d):Settings.Secure.getLong(r,k,d);}
 public static boolean putInt(ContentResolver r,String k,int v){return pen(k)?SystemSettings.putInt(r,k,v):Settings.Secure.putInt(r,k,v);}
 public static boolean putLong(ContentResolver r,String k,long v){return pen(k)?SystemSettings.putLong(r,k,v):Settings.Secure.putLong(r,k,v);}
 public static boolean putString(ContentResolver r,String k,String v){return pen(k)?SystemSettings.putString(r,k,v):Settings.Secure.putString(r,k,v);}
 public static Uri getUriFor(String k){return pen(k)?SystemSettings.getUriFor(k):Settings.Secure.getUriFor(k);}
}
