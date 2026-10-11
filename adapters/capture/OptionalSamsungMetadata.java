package org.evox.spen.capture;
import java.io.File;import android.os.ParcelFileDescriptor;
/** Samsung's optional SEF payload is separate from the JPEG and MediaStore commit. */
public final class OptionalSamsungMetadata {
 private static boolean warned;
 private static Object invoke(String name,Class<?>[] types,Object... args){try{return Class.forName("com.samsung.android.media.SemExtendedFormat").getMethod(name,types).invoke(null,args);}catch(ReflectiveOperationException|LinkageError unavailable){synchronized(OptionalSamsungMetadata.class){if(!warned){warned=true;android.util.Log.i("SamsungCapturePort","Samsung SEF metadata unavailable; standard image save remains active");}}return null;}}
 public static byte[] getData(File f,String key){return (byte[])invoke("getData",new Class[]{File.class,String.class},f,key);}
 public static int addData(File f,String key,byte[] bytes,int type,int option){Object v=invoke("addData",new Class[]{File.class,String.class,byte[].class,int.class,int.class},f,key,bytes,type,option);return v==null?0:((Number)v).intValue();}
 public static long addData(ParcelFileDescriptor f,String key,byte[] bytes,int type,int option){Object v=invoke("addData",new Class[]{ParcelFileDescriptor.class,String.class,byte[].class,int.class,int.class},f,key,bytes,type,option);return v==null?0:((Number)v).longValue();}
 public static int getDataCount(File f){Object v=invoke("getDataCount",new Class[]{File.class},f);return v==null?0:((Number)v).intValue();}
 public static String[] getKeyNameArray(File f){return (String[])invoke("getKeyNameArray",new Class[]{File.class},f);}
 public static int getDataType(File f,String key){Object v=invoke("getDataType",new Class[]{File.class,String.class},f,key);return v==null?0:((Number)v).intValue();}
 public static boolean hasData(File f,String key){return Boolean.TRUE.equals(invoke("hasData",new Class[]{File.class,String.class},f,key));}
 public static boolean deleteData(File f,String key){return Boolean.TRUE.equals(invoke("deleteData",new Class[]{File.class,String.class},f,key));}
}
