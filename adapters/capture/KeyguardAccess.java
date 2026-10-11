package org.evox.spen.compat;
import android.app.*;import android.content.*;import android.os.*;
public final class KeyguardAccess {
 public static boolean isDeviceSecure(Context c){KeyguardManager k=(KeyguardManager)c.getSystemService(Context.KEYGUARD_SERVICE);return k!=null&&k.isDeviceSecure();}
 public static boolean showing(KeyguardManager k){return k.isKeyguardLocked();}
 public static void dismiss(KeyguardManager k){if(!k.isKeyguardLocked())return;Activity a=RuntimeContext.active();if(a!=null)k.requestDismissKeyguard(a,null);else android.util.Log.i("AirCommandPort","Unlock required; no foreground original Activity for dismiss request");}
 public static void pending(KeyguardManager k,final PendingIntent pending,final Intent intent){final Context c=RuntimeContext.app();if(!k.isKeyguardLocked()){send(pending,intent);return;}c.registerReceiver(new BroadcastReceiver(){public void onReceive(Context context,Intent event){if(Intent.ACTION_USER_PRESENT.equals(event.getAction())){c.unregisterReceiver(this);send(pending,intent);}}},new IntentFilter(Intent.ACTION_USER_PRESENT),Context.RECEIVER_NOT_EXPORTED);dismiss(k);}
 private static void send(PendingIntent pending,Intent intent){try{pending.send(RuntimeContext.app(),0,intent);}catch(PendingIntent.CanceledException e){android.util.Log.w("AirCommandPort","Original pending launch was canceled");}}
 public static void waitForDraw(ActivityManager ignored){android.util.Log.d("AirCommandPort","Samsung draw-delay extension unavailable; Android manages keyguard draw synchronization");}
}
