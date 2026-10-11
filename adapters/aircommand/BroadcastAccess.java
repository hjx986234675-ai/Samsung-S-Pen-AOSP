package org.evox.spen.compat;
public final class BroadcastAccess {
 public static void send(android.content.Context ctx,android.content.Intent intent){
  if(android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction()) && ctx.checkSelfPermission("android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS")!=android.content.pm.PackageManager.PERMISSION_GRANTED){
   android.util.Log.d("AirCommandPort","No system-dialog closing permission; continue original shortcut launch");return;
  }
  ctx.sendBroadcast(intent);
 }
}
