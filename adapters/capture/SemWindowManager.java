package com.samsung.android.view;
import android.os.Handler;import android.graphics.*;import android.view.*;import java.util.*;import org.evox.spen.compat.RuntimeContext;
public final class SemWindowManager {
 private static final SemWindowManager INSTANCE=new SemWindowManager();
 public static SemWindowManager getInstance(){return INSTANCE;}
 public interface FoldStateListener {void onFoldStateChanged(boolean folded);void onTableModeChanged(boolean tableMode);}
 public static final class VisibleWindowInfo {public String name;public String packageName;public boolean focused;}
 public boolean isFolded(){return false;}public boolean isTableMode(){return false;}
 public void registerFoldStateListener(FoldStateListener l,Handler h){}public void unregisterFoldStateListener(FoldStateListener l){}
 public void getInitialDisplaySize(Point p){((android.hardware.display.DisplayManager)RuntimeContext.app().getSystemService(android.content.Context.DISPLAY_SERVICE)).getDisplay(Display.DEFAULT_DISPLAY).getRealSize(p);}
 public List<VisibleWindowInfo> getVisibleWindowInfoList(){
  try{String focus=org.evox.spen.compat.RootAccess.run("dumpsys window | grep -m 1 mCurrentFocus");
   java.util.regex.Matcher m=java.util.regex.Pattern.compile("u[0-9]+ ([^ /}]+)(?:/([^ }]+))?").matcher(focus);
   if(m.find()){VisibleWindowInfo info=new VisibleWindowInfo();info.packageName=m.group(1);info.name=m.group(2)==null?info.packageName:info.packageName+"/"+m.group(2);info.focused=true;return Collections.singletonList(info);}
  }catch(Exception failure){android.util.Log.w("SamsungCapturePort","Actual focused window query unavailable",failure);}
  return Collections.emptyList();
 }
 public boolean isSystemKeyEventRequested(int key,android.content.ComponentName name){return false;}
 public Bitmap screenshot(int display,int type,boolean identity,Rect crop,int width,int height,boolean rotation){return org.evox.spen.capture.NativeCapture.screenshot(display,crop,width,height);}
}
