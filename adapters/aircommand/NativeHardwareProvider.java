package org.evox.spen.compat;
import android.content.*;import android.database.Cursor;import android.net.Uri;import android.os.*;import java.io.*;import java.util.*;import java.util.concurrent.*;
/** Fixed hardware operations inside the original Air Command package; no caller-supplied shell commands. */
public final class NativeHardwareProvider extends ContentProvider {
 private static final String CAP="com.samsung.android.app.smartcapture",MAG="com.samsung.android.app.readingglass";
 private static final class Session{final int uid;volatile java.lang.Process process;volatile int pid;Session(int u){uid=u;}}
 private final Map<String,Session> sessions=new ConcurrentHashMap<>();
 public boolean onCreate(){return true;}
 private int caller(String expected){int uid=Binder.getCallingUid();android.content.pm.PackageManager pm=getContext().getPackageManager();String[] names=pm.getPackagesForUid(uid);if(names==null||!Arrays.asList(names).contains(expected)||pm.checkSignatures(uid,android.os.Process.myUid())!=android.content.pm.PackageManager.SIGNATURE_MATCH)throw new SecurityException("Only matching signed Samsung tool permitted");return uid;}
 private void close(String token,int uid){Session s=sessions.get(token);if(s==null)return;if(s.uid!=uid)throw new SecurityException("Stream owner mismatch");sessions.remove(token);if(s.pid>0)try{RootAccess.run("kill -TERM "+s.pid);}catch(Exception ignored){}if(s.process!=null)s.process.destroy();}
 public Bundle call(String method,String arg,Bundle extras){Bundle out=new Bundle();try{
  if("openPen".equals(method)){int uid=caller(MAG);for(String t:new ArrayList<>(sessions.keySet())){Session old=sessions.get(t);if(old!=null&&old.uid==uid)close(t,uid);}String token=UUID.randomUUID().toString();sessions.put(token,new Session(uid));out.putString("token",token);return out;}
  if("closePen".equals(method)){close(arg,caller(MAG));return out;}
  caller(CAP);
  if("closeSystemDialogs".equals(method))out.putString("result",RootAccess.run("am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS"));
  else if("foregroundWindow".equals(method))out.putString("result",RootAccess.run("dumpsys window | grep mCurrentFocus"));
  else throw new IllegalArgumentException("Unknown hardware operation");return out;
 }catch(IOException e){throw new IllegalStateException("Hardware request failed",e);}catch(InterruptedException e){Thread.currentThread().interrupt();throw new IllegalStateException(e);}}
 public ParcelFileDescriptor openFile(Uri uri,String mode)throws FileNotFoundException{
  if(!"r".equals(mode))throw new SecurityException("Read-only hardware stream");List<String> segments=uri.getPathSegments();final boolean capture=segments.size()==1&&"screenshot".equals(segments.get(0));final String token;final Session session;
  if(capture){caller(CAP);token=null;session=null;}else{int uid=caller(MAG);if(segments.size()!=2||!"pen".equals(segments.get(0)))throw new FileNotFoundException();token=segments.get(1);session=sessions.get(token);if(session==null||session.uid!=uid||session.process!=null)throw new SecurityException("Invalid pen session");}
  try{ParcelFileDescriptor[] pipe=ParcelFileDescriptor.createPipe();new Thread(()->{java.lang.Process process=null;try(OutputStream sink=new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])){
   if(session!=null&&!sessions.containsKey(token))return;
   process=new ProcessBuilder("su","-c",capture?"screencap -p":"echo SPEN_PID=$$; exec getevent -lt /dev/input/event5").start();
   if(session!=null){session.process=process;BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));String line=reader.readLine();if(line!=null&&line.startsWith("SPEN_PID="))session.pid=Integer.parseInt(line.substring(9));if(!sessions.containsKey(token)){if(session.pid>0)RootAccess.run("kill -TERM "+session.pid);return;}if(line!=null){sink.write((line+"\n").getBytes("UTF-8"));sink.flush();}while((line=reader.readLine())!=null){sink.write((line+"\n").getBytes("UTF-8"));sink.flush();}}
   else{try(InputStream in=process.getInputStream()){byte[] buffer=new byte[16384];int count;while((count=in.read(buffer))!=-1)sink.write(buffer,0,count);}if(process.waitFor()!=0)throw new IOException("Capture failed");}
  }catch(Exception e){android.util.Log.w("AirCommandPort","Hardware stream ended: "+e.getClass().getSimpleName());}finally{if(process!=null)process.destroy();if(session!=null)close(token,session.uid);}},capture?"Samsung-native-capture":"Samsung-native-pen-stream").start();return pipe[0];}catch(IOException e){throw new FileNotFoundException(e.toString());}
 }
 public Cursor query(Uri u,String[]p,String s,String[]a,String o){throw new UnsupportedOperationException();}public String getType(Uri u){return "image/png";}public Uri insert(Uri u,ContentValues v){throw new UnsupportedOperationException();}public int delete(Uri u,String s,String[]a){throw new UnsupportedOperationException();}public int update(Uri u,ContentValues v,String s,String[]a){throw new UnsupportedOperationException();}
}
