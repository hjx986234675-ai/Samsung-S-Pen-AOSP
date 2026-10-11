package org.evox.spen.compat;
public final class StatusBarAccess {
 public static void collapse(android.content.Context ctx){
  Object m=ctx.getSystemService("statusbar");if(m==null)return;
  try{m.getClass().getMethod("collapsePanels").invoke(m);}catch(ReflectiveOperationException e){android.util.Log.d("AirCommandPort","Status bar collapse unavailable for app; shortcut launch continues");}
 }
 public static boolean expanded(android.content.Context ctx){
  Object m=ctx.getSystemService("statusbar");if(m==null)return false;
  try{return (Boolean)m.getClass().getMethod("isPanelExpanded").invoke(m);}catch(ReflectiveOperationException e){android.util.Log.d("AirCommandPort","Global status bar expansion query unavailable");return false;}
 }
}
