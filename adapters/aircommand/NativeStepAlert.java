package org.evox.spen.compat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import java.util.IdentityHashMap;

/** Maps Samsung's eight-step alert to real Android step sensors, inside Air Command. */
public final class NativeStepAlert {
 public interface Listener { void onStepAlert(int type); }
 private final Context context;
 private final SensorManager sensors;
 private final Sensor sensor;
 private final IdentityHashMap<Listener, Watch> watches = new IdentityHashMap<>();
 public NativeStepAlert(Context c) {
  context=c.getApplicationContext();
  sensors=(SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
  Sensor s=sensors==null?null:sensors.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR,true);
  if(s==null && sensors!=null)s=sensors.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
  if(s==null && sensors!=null)s=sensors.getDefaultSensor(Sensor.TYPE_STEP_COUNTER,true);
  if(s==null && sensors!=null)s=sensors.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
  sensor=s;
 }
 public static NativeStepAlert obtain(Context c) { return new NativeStepAlert(c); }
 public static boolean available(Context c) { return new NativeStepAlert(c).isAvailableService(3); }
 public boolean isAvailableService(int type) {
  return type==3 && sensor!=null && context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)==PackageManager.PERMISSION_GRANTED;
 }
 public synchronized boolean registerListener(Listener listener,int type,int steps) {
  unregisterListener(listener,type);
  if(!isAvailableService(type))return false;
  Watch watch=new Watch(listener,steps);
  if(!sensors.registerListener(watch,sensor,SensorManager.SENSOR_DELAY_NORMAL,new Handler(Looper.getMainLooper())))return false;
  watches.put(listener,watch);
  if(!sensor.isWakeUpSensor()) {
   watch.wake=((PowerManager)context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"AirCmd:NativeStepAlert");
   watch.wake.acquire();
  }
  Log.i("AirCommandPort","Original pen reminder armed: real "+sensor.getName()+", threshold="+steps+", wakeup="+sensor.isWakeUpSensor());
  return true;
 }
 public synchronized void unregisterListener(Listener listener,int type) {
  Watch watch=watches.remove(listener);
  if(watch!=null){sensors.unregisterListener(watch);if(watch.wake!=null && watch.wake.isHeld())watch.wake.release();}
 }
 private final class Watch implements SensorEventListener {
  final Listener listener;final StepThreshold count;PowerManager.WakeLock wake;
  Watch(Listener l,int steps){listener=l;count=new StepThreshold(steps);}
  public void onAccuracyChanged(Sensor s,int accuracy){}
  public void onSensorChanged(SensorEvent e){
   if(count.sample(e.sensor.getType()==Sensor.TYPE_STEP_COUNTER,e.values[0])){
    unregisterListener(listener,3);
    Log.i("AirCommandPort","Real step threshold reached; dispatching original Samsung pen reminder");
    listener.onStepAlert(3);
   }
  }
 }
}
