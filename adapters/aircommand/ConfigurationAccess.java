package org.evox.spen.compat;
import android.content.res.Configuration;
public final class ConfigurationAccess {
 public static Configuration global(android.content.Context c){return new Configuration(c.getApplicationContext().getResources().getConfiguration());}

 public static boolean isPopOver(Configuration c){try{return (Boolean)c.getClass().getMethod("semIsPopOver").invoke(c);}catch(ReflectiveOperationException e){return false;}}
 public static int displayType(Configuration c){try{return c.getClass().getField("semDisplayDeviceType").getInt(c);}catch(ReflectiveOperationException e){return -1;}}
}
