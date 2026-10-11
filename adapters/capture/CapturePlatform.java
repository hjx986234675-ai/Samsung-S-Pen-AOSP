package org.evox.spen.capture;
import android.app.*;import android.content.*;import android.content.pm.*;import android.content.res.Configuration;import android.os.*;import android.view.*;import android.graphics.*;import org.evox.spen.compat.*;
/** Maps original Samsung calls to equivalent platform operations for the current Android user. */
public final class CapturePlatform {
 public static int lidState(android.hardware.input.InputManager manager){return -1;}
 public static String fontPath(Context context,int style){String path="/system/fonts/Roboto-Regular.ttf";return new java.io.File(path).isFile()?path:null;}
 public static boolean moveFile(android.os.storage.StorageManager manager,String from,String to){try{java.nio.file.Files.move(java.nio.file.Paths.get(from),java.nio.file.Paths.get(to),java.nio.file.StandardCopyOption.REPLACE_EXISTING);return true;}catch(java.io.IOException e){android.util.Log.w("SamsungCapturePort","App file move failed",e);return false;}}
 public static int patternCount(android.os.Vibrator v){return 0;}
 public static int vibrationType(android.os.Vibrator v){return 0;}
 public static android.os.VibrationEffect waveform(int pattern,int repeat,Object magnitude){return android.os.VibrationEffect.createPredefined(android.os.VibrationEffect.EFFECT_CLICK);}
 public static android.os.VibrationAttributes.Builder vibrationTag(android.os.VibrationAttributes.Builder b,String tag){return b;}

 public static void broadcast(Context c,Intent intent){
  if(Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())){new Thread(()->{try{RootAccess.run("am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS");}catch(Exception failure){android.util.Log.w("SamsungCapturePort","System dialog close unavailable",failure);}},"SamsungCaptureSystemDialogs").start();return;}
  c.sendBroadcast(intent);
 }

 public static int settingInt(android.content.ContentResolver resolver,String key,int fallback,int user){if(user>=0&&user!=UserAccess.myId())throw new SecurityException("Capture cross-user settings read unavailable");return android.provider.Settings.System.getInt(resolver,key,fallback);}
 public static void init(Context c){RuntimeContext.init(c);}
 public static int identifier(UserHandle u){Parcel p=Parcel.obtain();try{u.writeToParcel(p,0);p.setDataPosition(0);return p.readInt();}finally{p.recycle();}}
 private static void current(UserHandle u){int id=identifier(u);if(id>=0&&id!=UserAccess.myId())throw new SecurityException("Capture cross-user operation is unavailable");}
 public static ComponentName start(Context c,Intent i,UserHandle u){current(u);return c.startService(i);}
 public static boolean bind(Context c,Intent i,ServiceConnection s,int flags,UserHandle u){current(u);return c.bindService(i,s,flags);}
 public static Context packageContext(Context c,String name,int flags,UserHandle u)throws PackageManager.NameNotFoundException{current(u);return c.createPackageContext(name,flags);}
 public static int feature(PackageManager pm,String name){FeatureInfo[] features=pm.getSystemAvailableFeatures();if(features!=null)for(FeatureInfo f:features)if(name.equals(f.name))return f.version;return 0;}
 public static android.graphics.drawable.Drawable icon(PackageManager pm,ComponentName component,int tray)throws PackageManager.NameNotFoundException{return pm.getActivityIcon(component);}
 public static int desktop(Configuration c){return 0;} // EvolutionX does not implement Samsung DeX.
 public static int keyboardCovered(Configuration c){return 0;} // SM-T970 has no Samsung covered-keyboard sensor.
 public static int displayType(Configuration c){return 0;} // Single non-foldable tablet display.
 public static boolean popover(Configuration c){return false;}
 public static void buttonShape(android.widget.TextView view,boolean enabled,int color){android.util.Log.d("SamsungCapturePort","Button shape follows original drawable and Android theme");}
 public static void multiSelection(android.widget.TextView view,boolean enabled){view.setTextIsSelectable(enabled);}
 public static void toast(android.widget.Toast toast,int display){android.util.Log.d("SamsungCapturePort","Toast uses the original context display");}
 public static boolean hideKeyboard(android.view.inputmethod.InputMethodManager manager){Activity a=RuntimeContext.active();return a!=null&&a.getCurrentFocus()!=null&&manager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(),0);}
 public static boolean accessoryKeyboard(android.view.inputmethod.InputMethodManager manager){return RuntimeContext.app().getResources().getConfiguration().keyboard!=Configuration.KEYBOARD_NOKEYS;}
 public static void launchOver(Intent i,int task,boolean force){android.util.Log.d("SamsungCapturePort","Original activity launches using Android task policy");}
 public static int vibrationIndex(int type){return HapticFeedbackConstants.CONTEXT_CLICK;}
 public static void soundSituation(android.media.SoundPool pool,int sound,String situation){android.util.Log.d("SamsungCapturePort","SoundPool uses Android audio attributes");}
 public static void soundStream(android.media.SoundPool pool,int stream){android.util.Log.d("SamsungCapturePort","SoundPool stream uses creation-time audio attributes");}
}
