package org.evox.spen.capture;
import android.content.Context;import android.media.*;import android.os.*;
public final class CaptureFeedback {
 public static void sound(Context context,int kind){if(context==null)return;try{
  String asset=kind==0?"samsung-screen-capture.ogg":null;if(asset==null)return;
  android.content.res.AssetFileDescriptor fd=context.getAssets().openFd(asset);MediaPlayer player=new MediaPlayer();player.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());player.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());fd.close();player.setOnCompletionListener(p->p.release());player.setOnErrorListener((p,what,extra)->{p.release();return true;});player.prepare();player.start();
 }catch(Exception failure){android.util.Log.w("SamsungCapturePort","Original shutter sound unavailable",failure);}}
 public static void vibrate(Context c){if(c==null)return;Vibrator v=(Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);if(v!=null&&v.hasVibrator())v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));}
}
