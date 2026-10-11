package org.evox.spen.compat;
import android.content.Context;import android.os.*;import android.media.AudioAttributes;import android.provider.Settings;
/** Standard Android feedback at the original Samsung feedback call sites. */
public final class HapticAccess {
 public static void play(Context context,int pattern){VibratorManager manager=context.getSystemService(VibratorManager.class);if(manager==null)return;Vibrator vibrator=manager.getDefaultVibrator();if(!vibrator.hasVibrator())return;if(pattern!=7&&Settings.System.getInt(context.getContentResolver(),Settings.System.HAPTIC_FEEDBACK_ENABLED,1)==0)return;VibrationEffect effect=pattern==7?VibrationEffect.createWaveform(new long[]{0,200,100,200},-1):VibrationEffect.createPredefined(pattern==127?VibrationEffect.EFFECT_TICK:VibrationEffect.EFFECT_CLICK);AudioAttributes attributes=new AudioAttributes.Builder().setUsage(pattern==7?AudioAttributes.USAGE_ALARM:AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build();vibrator.vibrate(effect,attributes);}
}
