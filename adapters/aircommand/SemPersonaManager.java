package com.samsung.android.knox;
/** EvoX has no Knox persona service; no Android user is a Knox persona here. */
public final class SemPersonaManager {
 public static boolean isKnoxId(int user){return false;}
 public int getFocusedKnoxId(){return org.evox.spen.compat.UserAccess.currentId();}
}
