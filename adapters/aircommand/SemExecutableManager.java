package com.samsung.android.app;
/** API type only. EvoX has no execute service; original null checks remain effective. */
public abstract class SemExecutableManager {
 public abstract java.util.List<SemExecutableInfo> getExecutableInfos();
 public abstract SemExecutableInfo getExecutableInfo(String id);
}
