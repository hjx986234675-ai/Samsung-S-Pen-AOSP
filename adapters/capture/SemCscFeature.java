package com.samsung.android.feature;
/** EvoX has no CSC feature backend; preserve API absent-value defaults. */
public final class SemCscFeature{private static final SemCscFeature instance=new SemCscFeature();public static SemCscFeature getInstance(){return instance;}public String getString(String key){return "";}public String getString(String key,String def){return def;}public boolean getBoolean(String key){return false;}public boolean getBoolean(String key,boolean def){return def;}public int getInt(String key){return -1;}public int getInt(String key,int def){return def;}}
