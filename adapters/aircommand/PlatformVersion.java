package org.evox.spen.compat;
public final class PlatformVersion {
 public static int get() {
  try { return android.os.Build.VERSION.class.getField("SEM_PLATFORM_INT").getInt(null); }
  catch (ReflectiveOperationException e) { return android.os.SemSystemProperties.getInt("ro.build.version.sem", 0); }
 }
}
