package com.samsung.android.view;
import android.os.Handler;import android.graphics.*;import java.util.*;
/** Target SM-T970 is not foldable. Capture and cross-app window backends remain unsupported. */
public final class SemWindowManager {
 private static final SemWindowManager INSTANCE=new SemWindowManager();
 public static SemWindowManager getInstance(){return INSTANCE;}
 public interface FoldStateListener {void onFoldStateChanged(boolean folded);void onTableModeChanged(boolean tableMode);}
 public static final class VisibleWindowInfo {public String name;public String packageName;}
 public boolean isTableMode(){return false;}
 public void registerFoldStateListener(FoldStateListener l,Handler h){}
 public void unregisterFoldStateListener(FoldStateListener l){}
 public List<VisibleWindowInfo> getVisibleWindowInfoList(){return Collections.emptyList();}
 public Bitmap screenshot(int display,int type,boolean identity,Rect crop,int width,int height,boolean rotation){android.util.Log.w("AirCommandPort","Samsung window screenshot backend is not ported");return null;}
}
