package org.evox.spen.compat;
import android.content.ContentResolver;
import android.provider.Settings;
import android.net.Uri;
public final class SystemSettings {
 private static boolean custom(String k){return k.startsWith("pen_")||k.startsWith("spen_")||k.startsWith("air_cmd_")||k.startsWith("air_command")||k.startsWith("aircommand")||k.equals("screen_off_memo");}
 private static String portKey(String k){return "evox_aircommand_"+k;}
 public static String getString(ContentResolver r,String k){
  if(k.equals("spen_unlock")||k.equals("screen_off_memo")||k.equals("spen_double_tap_launch"))return "0";
  if(custom(k))return Settings.Secure.getString(r,portKey(k));
  String v=Settings.System.getString(r,k);return v!=null?v:Settings.Secure.getString(r,portKey(k));
 }
 public static int getInt(ContentResolver r,String k,int d){try{return Integer.parseInt(getString(r,k));}catch(NumberFormatException e){return d;}}
 public static int getInt(ContentResolver r,String k)throws Settings.SettingNotFoundException{try{return Integer.parseInt(getString(r,k));}catch(NumberFormatException e){throw new Settings.SettingNotFoundException(k);}}
 public static long getLong(ContentResolver r,String k,long d){try{return Long.parseLong(getString(r,k));}catch(NumberFormatException e){return d;}}
 public static boolean putString(ContentResolver r,String k,String v){
  if(k.equals("spen_unlock")||k.equals("screen_off_memo")||k.equals("spen_double_tap_launch"))v="0";
  if(custom(k))return Settings.Secure.putString(r,portKey(k),v);
  try{return Settings.System.putString(r,k,v);}catch(IllegalArgumentException e){android.util.Log.i("AirCommandPort","Custom setting storage: "+k);return Settings.Secure.putString(r,portKey(k),v);}
 }
 public static boolean putInt(ContentResolver r,String k,int v){return putString(r,k,Integer.toString(v));}
 public static boolean putLong(ContentResolver r,String k,long v){return putString(r,k,Long.toString(v));}
 public static Uri getUriFor(String k){return custom(k)?Settings.Secure.getUriFor(portKey(k)):Settings.System.getUriFor(k);}
 public static int semGetIntForUser(ContentResolver r,String k,int d,int user){return SettingsAccess.getSystemIntForUser(r,k,d,user);}
}
