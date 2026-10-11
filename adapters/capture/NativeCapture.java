package org.evox.spen.capture;
import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Rect;import android.util.Log;import java.io.*;
/** Screen pixels stay in the Samsung app; capture is requested only by its capture workflow. */
public final class NativeCapture {
 public static Bitmap captureRect(android.content.Context c,int display,Rect crop,int width,int height){Bitmap result=screenshot(display,crop,width,height);if(result!=null)result.setDensity(c.getResources().getDisplayMetrics().densityDpi);return result;}
 public static Bitmap captureScaled(android.view.Display display,float scale,int width,int height){return screenshot(display.getDisplayId(),null,width>0?Math.max(1,Math.round(width*scale)):0,height>0?Math.max(1,Math.round(height*scale)):0);}
 public static Bitmap screenshot(int display,Rect requested,int width,int height){
  if(display!=0){Log.e("SamsungCapturePort","Capture of secondary display is not adapted");return null;}
  android.os.ParcelFileDescriptor descriptor=null;
  try{
   descriptor=org.evox.spen.compat.RuntimeContext.app().getContentResolver().openFileDescriptor(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware/screenshot"),"r");
   Bitmap image;try(InputStream stream=new android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor)){image=BitmapFactory.decodeStream(stream);}descriptor=null;
   if(image==null)throw new IOException("Native capture denied or invalid image");
   if(requested!=null&&!requested.isEmpty()){
    Rect crop=new Rect(requested);
    if(!crop.intersect(0,0,image.getWidth(),image.getHeight())){image.recycle();return null;}
    Bitmap part=Bitmap.createBitmap(image,crop.left,crop.top,crop.width(),crop.height());
    if(part!=image)image.recycle();image=part;
   }
   if(width>0&&height>0&&(width!=image.getWidth()||height!=image.getHeight())){
    Bitmap scaled=Bitmap.createScaledBitmap(image,width,height,true);if(scaled!=image)image.recycle();image=scaled;
   }
   return image;
  }catch(IOException e){Log.e("SamsungCapturePort","Original capture backend failed",e);return null;}
  finally{if(descriptor!=null)try{descriptor.close();}catch(IOException ignored){}}
 }
}
