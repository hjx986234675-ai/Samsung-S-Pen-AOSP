package org.evox.spen.compat;
public final class PackageAccess {
 public static java.util.List<android.content.pm.ResolveInfo> query(android.content.pm.PackageManager pm,android.content.Intent i,int user){
  if(user>=0 && user!=UserAccess.myId())throw new SecurityException("AirCommand port only queries current user apps");
  return pm.queryIntentActivities(i,0);
 }
 public static java.util.List<android.content.pm.ResolveInfo> queryFlags(android.content.pm.PackageManager pm,android.content.Intent i,int flags,int user){if(user>=0&&user!=UserAccess.myId())throw new SecurityException("Cross-user query unsupported");return pm.queryIntentActivities(i,flags);}
}
