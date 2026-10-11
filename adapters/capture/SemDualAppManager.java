package com.samsung.android.app;
/** Samsung Dual Messenger is unavailable on EvoX. */
public final class SemDualAppManager {
 private static final SemDualAppManager INSTANCE=new SemDualAppManager();
 public static SemDualAppManager getInstance(android.content.Context context){return INSTANCE;}
 public static boolean isDualAppId(int user){return false;}
 public static int getDualAppProfileId(){return -10000;}
 public boolean isSupported(){return false;}
}
