package org.evox.spen.compat;
import java.io.*;import java.util.concurrent.TimeUnit;
public final class RootAccess {
 public static String quote(String s){return "'"+s.replace("'","'\\''")+"'";}
 public static String run(String command)throws IOException,InterruptedException{
  String method;
  if("am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(command))method="closeSystemDialogs";
  else if("dumpsys window | grep -m 1 mCurrentFocus".equals(command))method="foregroundWindow";
  else throw new IOException("Unsupported hardware operation");
  try{android.os.Bundle out=RuntimeContext.app().getContentResolver().call(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware"),method,null,null);if(out==null)throw new IOException("Original hardware provider unavailable");return out.getString("result","");}catch(RuntimeException failure){throw new IOException("Hardware provider refused request",failure);}
 }
}
