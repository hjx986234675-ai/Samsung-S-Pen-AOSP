package org.evox.spen.compat;
public final class UserAccess {
 public static int myId(){return android.os.Process.myUid()/100000;}
 public static int callingId(){return android.os.Binder.getCallingUid()/100000;}
 public static android.os.UserHandle all(){return of(-1);}
 public static android.os.UserHandle current(){return of(-2);}
 public static android.os.UserHandle of(int user){
  android.os.Parcel p=android.os.Parcel.obtain();
  try {p.writeInt(user);p.setDataPosition(0);return android.os.UserHandle.CREATOR.createFromParcel(p);}finally{p.recycle();}
 }

 public static int currentId() {
  try {return (Integer)android.app.ActivityManager.class.getMethod("getCurrentUser").invoke(null);}
  catch(ReflectiveOperationException e) { return android.os.Process.myUid()/100000; }
 }
}
