package org.evox.spen.compat;
import android.content.*;import android.database.Cursor;import android.net.Uri;import android.os.*;
public class NativeMagnificationProvider extends ContentProvider {
 public boolean onCreate(){return true;}
 public Bundle call(String method,String arg,Bundle extras){
  if(getContext().getPackageManager().checkSignatures(Binder.getCallingUid(),android.os.Process.myUid())!=android.content.pm.PackageManager.SIGNATURE_MATCH)throw new SecurityException("Only the matching signed S Pen port can control magnification");
  Bundle result=new Bundle();NativeMagnificationService service=NativeMagnificationService.instance;
  result.putBoolean("connected",service!=null);
  if("update".equals(method)&&extras!=null&&service!=null){service.update(new Bundle(extras));result.putBoolean("queued",true);}
  return result;
 }
 public Cursor query(Uri u,String[]p,String s,String[]a,String o){throw new UnsupportedOperationException();}
 public String getType(Uri u){return null;}
 public Uri insert(Uri u,ContentValues v){throw new UnsupportedOperationException();}
 public int delete(Uri u,String s,String[]a){throw new UnsupportedOperationException();}
 public int update(Uri u,ContentValues v,String s,String[]a){throw new UnsupportedOperationException();}
}
