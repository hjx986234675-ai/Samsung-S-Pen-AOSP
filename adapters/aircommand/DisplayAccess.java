package org.evox.spen.compat;
import android.hardware.display.DisplayManager;import android.view.Display;import android.widget.Toast;import java.util.regex.*;
public final class DisplayAccess {
 private static volatile int focused=0;
 public static int focusedId(){return focused;}
 public static void refresh(){try{Matcher m=Pattern.compile("mTopFocusedDisplayId=([0-9]+)").matcher(RootAccess.run("dumpsys window | grep -m 1 mTopFocusedDisplayId"));if(m.find())focused=Integer.parseInt(m.group(1));}catch(Exception unavailable){android.util.Log.d("AirCommandPort","Focused display query unavailable; tablet display used");}}
 public static int wifiState(DisplayManager dm){try{Object status=dm.getClass().getMethod("getWifiDisplayStatus").invoke(dm);int state=(Integer)status.getClass().getMethod("getActiveDisplayState").invoke(status);return state==2?3:0;}catch(ReflectiveOperationException unavailable){try{String state=RootAccess.run("dumpsys display | grep -m 1 mWifiDisplayStatus");return state.contains("activeDisplayState=2")?3:0;}catch(Exception failed){android.util.Log.w("AirCommandPort","Wifi display status unavailable");return 0;}}}
 public static void toast(Toast toast,int type){android.util.Log.d("AirCommandPort","Toast uses its Android context display; Samsung preferred display type="+type);}
}
