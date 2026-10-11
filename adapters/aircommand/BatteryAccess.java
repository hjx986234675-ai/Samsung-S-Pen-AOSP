package org.evox.spen.compat;
import android.os.BatteryManager;import java.util.concurrent.ConcurrentHashMap;import java.lang.reflect.Array;
public final class BatteryAccess {
 static final ConcurrentHashMap<String,Object> reports=new ConcurrentHashMap<>();
 public static void set(BatteryManager ignored,String address,Object info){reports.put(address,info);android.util.Log.i("AirCommandPort","Native S Pen battery report retained locally; One UI system battery widget absent");}
 public static void unset(BatteryManager ignored,String address){reports.remove(address);}
 public static Object get(BatteryManager ignored){try{Class<?> type=Class.forName("com.samsung.android.os.SemCompanionDeviceBatteryInfo");Object array=Array.newInstance(type,reports.size());int i=0;for(Object value:reports.values())Array.set(array,i++,value);return array;}catch(Exception e){throw new IllegalStateException(e);}}
}
