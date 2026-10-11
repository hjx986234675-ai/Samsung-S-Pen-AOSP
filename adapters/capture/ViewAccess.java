package org.evox.spen.compat;
import android.view.*;
public final class ViewAccess {
 public static boolean screenReader(android.view.accessibility.AccessibilityManager m){return m.isEnabled()&&!m.getEnabledAccessibilityServiceList(android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_SPOKEN).isEmpty();}
 public static void hoverPopup(final View v,int type){
  if(type==0){v.setTooltipText(null);return;}
  v.post(new Runnable(){public void run(){v.setTooltipText(v.getContentDescription());}});
 }
 public static void pointer(View v,int tool,PointerIcon icon){v.setPointerIcon(icon);}
 public static void rounded(final View v,int corners){
  if(corners==0){v.setClipToOutline(false);return;}
  if(corners!=15){android.util.Log.d("AirCommandPort","Partial Samsung corner mask unsupported: "+corners);return;}
  int id=v.getResources().getIdentifier("sesl_rounded_corner_radius","dimen",v.getContext().getPackageName());
  final float radius=id!=0?v.getResources().getDimension(id):26*v.getResources().getDisplayMetrics().density;
  v.setOutlineProvider(new ViewOutlineProvider(){public void getOutline(View view,android.graphics.Outline o){o.setRoundRect(0,0,view.getWidth(),view.getHeight(),radius);}});
  v.setClipToOutline(true);
 }
 public static void cornerColor(View v,int corners,int color){android.util.Log.d("AirCommandPort","Samsung corner color masking unsupported; retain original view background");}
}
