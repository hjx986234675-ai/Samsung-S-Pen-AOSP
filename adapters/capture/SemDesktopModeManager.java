package com.samsung.android.desktopmode;
public final class SemDesktopModeManager {
 public interface DesktopModeListener {void onDesktopModeStateChanged(SemDesktopModeState state);}
 public SemDesktopModeState getDesktopModeState(){return new SemDesktopModeState();}
 public void registerListener(DesktopModeListener listener){}public void unregisterListener(DesktopModeListener listener){}
}
