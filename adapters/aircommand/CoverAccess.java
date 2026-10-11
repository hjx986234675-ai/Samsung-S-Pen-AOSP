package org.evox.spen.compat;
import android.content.Context;
public final class CoverAccess {
 public static Object state(Context c){try{Class.forName("com.samsung.android.cover.ICoverManager",false,c.getClassLoader());Class<?> api=Class.forName("com.samsung.android.sdk.cover.ScoverManager",false,c.getClassLoader());Object manager=api.getConstructor(Context.class).newInstance(c);return api.getMethod("getCoverState").invoke(manager);}catch(ReflectiveOperationException|LinkageError missing){android.util.Log.d("AirCommandPort","Samsung cover state unavailable; original unknown-state branch used");return null;}}
}
