package org.evox.spen.capture;
import android.content.Context;import android.os.Bundle;
public final class OptionalKnox {
 public static Bundle info(Context c){try{return (Bundle)Class.forName("com.samsung.android.knox.SemPersonaManager").getMethod("getKnoxInfoForApp",Context.class).invoke(null,c);}catch(ClassNotFoundException absent){android.util.Log.i("SamsungCapturePort","Knox unavailable; standard current-user sharing");return null;}catch(ReflectiveOperationException failure){throw new IllegalStateException("Knox query failed",failure);}}
}
