package org.evox.spen.compat;
import android.view.View;import android.os.*;import android.graphics.Point;import java.lang.reflect.*;
public final class MenuPlacement {
 static Object field(Object o,String name)throws Exception{Field f=o.getClass().getField(name);return f.get(o);}
 public static void prepare(final Object controller,final Object params){
  try{final Point p=(Point)field(params,"buttonClickPos");final Handler h=new Handler(Looper.getMainLooper());final Runnable place=()->{try{
   if(!controller.getClass().getName().equals("m4.e"))return;
   final View content=(View)field(field(field(controller,"h"),"i"),"o");if(content==null)return;
   View root=content.getRootView();String pkg=root.getContext().getPackageName();
   View v=root.findViewById(root.getResources().getIdentifier("compact_main_view","id",pkg));
   View settings=root.findViewById(root.getResources().getIdentifier("setting_container","id",pkg));
   if(v==null||settings==null)return;
   if(p==null){v.setTranslationX(0);v.setTranslationY(0);settings.setTranslationX(0);settings.setTranslationY(0);return;}
   if(!v.isAttachedToWindow()||v.getWidth()==0)return;
   int[] loc=new int[2],sl=new int[2],base=new int[2];v.getLocationOnScreen(loc);settings.getLocationOnScreen(sl);root.getLocationOnScreen(base);
   int left=Math.min(loc[0],sl[0]),top=Math.min(loc[1],sl[1]);int width=Math.max(loc[0]+v.getWidth(),sl[0]+settings.getWidth())-left,height=Math.max(loc[1]+v.getHeight(),sl[1]+settings.getHeight())-top;
   float targetX=Math.max(base[0],Math.min(p.x-width/2f,base[0]+root.getWidth()-width));
   float targetY=Math.max(base[1],Math.min(p.y-height/2f,base[1]+root.getHeight()-height));
   float dx=targetX-left,dy=targetY-top;
   v.setTranslationX(v.getTranslationX()+dx);v.setTranslationY(v.getTranslationY()+dy);
   settings.setTranslationX(settings.getTranslationX()+dx);settings.setTranslationY(settings.getTranslationY()+dy);
   android.util.Log.i("AirCommandPort","Original compact menu anchored at "+p+" bounds "+targetX+","+targetY+","+width+","+height);
  }catch(Exception e){android.util.Log.w("AirCommandPort","Menu placement failed",e);}};
  h.postDelayed(place,80);h.postDelayed(place,450);
  }catch(Exception e){android.util.Log.w("AirCommandPort","Cannot read original menu position",e);}
 }
}
