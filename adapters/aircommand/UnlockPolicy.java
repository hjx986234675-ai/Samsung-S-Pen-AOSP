package org.evox.spen.compat;
import android.content.*;
/** The user excluded S Pen unlock; block only this package's trust-agent launch. */
public final class UnlockPolicy {
 public static void apply(Context context){
  for(String name:new String[]{"com.samsung.android.service.aircommand.svc.remotespentrustagent.RemoteSpenTrustAgentService","com.samsung.android.service.aircommand.ui.settings.unlock.UnlockConfirmationActivity"}){
   ComponentName component=new ComponentName(context.getPackageName(),name);
   context.getPackageManager().setComponentEnabledSetting(component,android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,android.content.pm.PackageManager.DONT_KILL_APP);
  }
  android.util.Log.i("AirCommandPort","Excluded S Pen unlock components disabled by original package");
 }
 public static boolean disabled(Intent intent){ComponentName c=intent.getComponent();return c!=null&&c.getClassName().equals("com.samsung.android.service.aircommand.svc.remotespentrustagent.RemoteSpenTrustAgentService")||intent.getAction()!=null&&intent.getAction().startsWith("com.samsung.android.service.aircommand.action.TRUST_AGENT_");}
 public static ComponentName start(Context context,Intent intent){if(disabled(intent)){android.util.Log.i("AirCommandPort","Excluded S Pen unlock service launch blocked");return null;}return context.startService(intent);}
}
