package org.evox.spen.compat;
import android.view.*;import java.io.*;import java.lang.reflect.*;
/** Root worker loaded from the original signed APK; no separate bridge application. */
public final class RootInputMain {
 public static void main(String[] args)throws Exception{
  Class<?> vm=Class.forName("dalvik.system.VMRuntime");Object runtime=vm.getDeclaredMethod("getRuntime").invoke(null);vm.getDeclaredMethod("setHiddenApiExemptions",String[].class).invoke(runtime,(Object)new String[]{"L"});
  Class<?> cls=Class.forName("android.hardware.input.InputManagerGlobal");Object manager=cls.getDeclaredMethod("getInstance").invoke(null);Method inject=cls.getDeclaredMethod("injectInputEvent",InputEvent.class,int.class);Method display=InputEvent.class.getDeclaredMethod("setDisplayId",int.class);display.setAccessible(true);
  Class<?> sm=Class.forName("android.os.ServiceManager");android.os.IBinder binder=(android.os.IBinder)sm.getDeclaredMethod("getService",String.class).invoke(null,"statusbar");
  Class<?> stub=Class.forName("com.android.internal.statusbar.IStatusBarService$Stub");Object statusbar=stub.getDeclaredMethod("asInterface",android.os.IBinder.class).invoke(null,binder);
  Method recent=Class.forName("com.android.internal.statusbar.IStatusBarService").getMethod("toggleRecentApps");
  PrintWriter out=new PrintWriter(System.out,true);out.println("READY");try(BufferedReader in=new BufferedReader(new InputStreamReader(System.in))){String row;while((row=in.readLine())!=null){try{String[] a=row.split(" ");if(a.length!=11)throw new IllegalArgumentException();KeyEvent e=new KeyEvent(Long.parseLong(a[0]),Long.parseLong(a[1]),Integer.parseInt(a[2]),Integer.parseInt(a[3]),Integer.parseInt(a[4]),Integer.parseInt(a[5]),Integer.parseInt(a[6]),Integer.parseInt(a[7]),Integer.parseInt(a[8]),Integer.parseInt(a[9]));if(e.getKeyCode()==187){if(e.getAction()==KeyEvent.ACTION_UP&&!e.isCanceled()&&e.getRepeatCount()==0)recent.invoke(statusbar);out.println("OK");continue;}display.invoke(e,Integer.parseInt(a[10]));out.println(Boolean.TRUE.equals(inject.invoke(manager,e,2))?"OK":"REJECTED");}catch(Throwable failure){out.println("ERROR_"+failure.getClass().getSimpleName());}}}
 }
}
