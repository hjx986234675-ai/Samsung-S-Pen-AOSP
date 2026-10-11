package com.samsung.android.os;
/** Samsung-specific performance hints are unavailable; original null-manager paths remain in use. */
public final class SemDvfsManager {
 public static SemDvfsManager createInstance(android.content.Context c){return null;}
 public static SemDvfsManager createInstance(android.content.Context c,String tag){return null;}
 public static boolean checkHintSupported(int hint){return false;}
 public void acquire(){} public void acquire(int timeout){} public void release(){} public void setHint(int hint){} public void addResourceValue(int resource,int value){}
 public int getApproximateFrequency(int value,int resource,int mode){return 0;}public int[] getSupportedFrequency(int resource,int mode){return null;}
}
