package org.evox.spen.compat;
import android.bluetooth.*;
public final class RootBluetoothMain {
 public static void main(String[] args)throws Exception{Class<?> vm=Class.forName("dalvik.system.VMRuntime");Object runtime=vm.getDeclaredMethod("getRuntime").invoke(null);vm.getDeclaredMethod("setHiddenApiExemptions",String[].class).invoke(runtime,(Object)new String[]{"L"});if(args.length!=1||!BluetoothAdapter.checkBluetoothAddress(args[0]))throw new IllegalArgumentException("Invalid pen address");BluetoothDevice d=BluetoothAdapter.getDefaultAdapter().getRemoteDevice(args[0]);System.out.println(Boolean.TRUE.equals(BluetoothDevice.class.getMethod("removeBond").invoke(d))?"OK":"REJECTED");}
}
