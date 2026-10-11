package org.evox.spen.capture;
import android.content.ContentResolver;import android.content.ContentValues;import android.net.Uri;
/** Original Samsung Gallery indexing is optional after the public MediaStore commit. */
public final class OptionalSamsungMedia {
 public static Uri insert(ContentResolver resolver,Uri uri,ContentValues values){
  if(!"secmedia".equals(uri.getAuthority()))return resolver.insert(uri,values);
  try{return resolver.insert(uri,values);}catch(IllegalArgumentException unavailable){android.util.Log.i("SamsungCapturePort","Samsung Gallery metadata provider unavailable; public MediaStore image retained");return null;}
 }
}
