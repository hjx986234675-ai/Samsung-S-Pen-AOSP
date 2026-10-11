package org.evox.spen.compat;
import android.content.*;import android.content.pm.*;import android.telecom.TelecomManager;import android.telephony.TelephonyManager;import android.os.PowerManager;import android.view.*;import android.widget.Button;import android.text.TextPaint;
public final class PlatformAccess {
 public static boolean videoCall(TelephonyManager ignored){Context c=RuntimeContext.app();if(!c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY))return false;try{return ((TelecomManager)c.getSystemService(Context.TELECOM_SERVICE)).isInCall();}catch(SecurityException unsupported){android.util.Log.w("AirCommandPort","Video-call rule unavailable without Android call-state permission");return false;}}
 public static int featureLevel(PackageManager pm,String name){FeatureInfo[] features=pm.getSystemAvailableFeatures();if(features!=null)for(FeatureInfo f:features)if(name.equals(f.name))return f.version;return 0;}
 public static void buttonShape(Button b,boolean enabled){android.util.Log.d("AirCommandPort","Samsung button-shape extension unavailable; original button drawable retained");}
 public static char[] spanPrefix(TextPaint p,CharSequence s,char[] text){return null;/* original caller's null branch keeps its original text */}
 public static void wake(PowerManager power,long time,int reason,String detail){if(power.isInteractive())return;long now=android.os.SystemClock.uptimeMillis();InputAccess.inject(new KeyEvent(now,now,KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_WAKEUP,0));InputAccess.inject(new KeyEvent(now,now,KeyEvent.ACTION_UP,KeyEvent.KEYCODE_WAKEUP,0));}
 public static boolean proximityAvailable(Context c){return NativeStepAlert.available(c);}
}
