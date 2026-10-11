package org.evox.spen.port;
import android.content.Context;import android.graphics.Point;import android.os.*;import android.view.*;import android.util.Log;import java.io.*;import java.lang.reflect.Method;import java.util.regex.*;
public final class RootHoverInput {
 private static volatile String streamToken;private static volatile int generation;private static InputStream stream;private static Thread thread;private static volatile boolean running;
 private static final Handler main=new Handler(Looper.getMainLooper());private static final Object token=new Object();
 public static synchronized void start(final Context service){
  stop(service);running=true;final int session=++generation;
  thread=new Thread(()->{
   try{
    Method hover=service.getClass().getMethod("onHoverEvent",int.class,int.class,int.class);
    Bundle opened=service.getContentResolver().call(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware"),"openPen",null,null);
    if(opened==null||opened.getString("token")==null)throw new IOException("Pen hardware service unavailable");
    String tokenValue=opened.getString("token");
    synchronized(RootHoverInput.class){if(generation!=session){service.getContentResolver().call(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware"),"closePen",tokenValue,null);return;}streamToken=tokenValue;}
    android.os.ParcelFileDescriptor descriptor=service.getContentResolver().openFileDescriptor(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware/pen/"+tokenValue),"r");
    InputStream localStream=new android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor);
    synchronized(RootHoverInput.class){if(generation!=session){localStream.close();return;}stream=localStream;}
    BufferedReader input=new BufferedReader(new InputStreamReader(localStream));
    Pattern pattern=Pattern.compile("EV_(ABS|KEY|SYN)\\s+(\\w+)\\s+(\\w+)");
    int x=-1,y=-1;boolean near=false,touch=false,previous=false;long last=0;
    String line;
    while(running&&generation==session&&(line=input.readLine())!=null){
     if(line.startsWith("SPEN_PID="))continue;
     Matcher m=pattern.matcher(line);if(!m.find()){if(line.contains("denied")||line.contains("not found"))Log.e("SpenPort",line);continue;}
     String kind=m.group(1),key=m.group(2),value=m.group(3);
     int n=value.equals("DOWN")?1:value.equals("UP")?0:(int)Long.parseLong(value,16);
     if(kind.equals("ABS")){if(key.equals("ABS_X"))x=n;else if(key.equals("ABS_Y"))y=n;}
     else if(kind.equals("KEY")){if(key.equals("BTN_DIGI"))near=n!=0;else if(key.equals("BTN_TOUCH"))touch=n!=0;}
     else if(key.equals("SYN_DROPPED")){Log.e("SpenPort","Input state lost; restart required");break;}
     else if(key.equals("SYN_REPORT")&&x>=0&&y>=0){
      boolean active=near&&!touch;long now=SystemClock.uptimeMillis();if(active==previous&&(!active||now-last<32))continue;
      int action=active?(previous?7:9):10;previous=active;last=now;
      Display display=((WindowManager)service.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();Point size=new Point();display.getRealSize(size);
      float u=Math.max(0,Math.min(1,x/16714f)),v=Math.max(0,Math.min(1,y/26712f)),sx=u,sy=v;
      switch(display.getRotation()){case Surface.ROTATION_90:sx=v;sy=1-u;break;case Surface.ROTATION_180:sx=1-u;sy=1-v;break;case Surface.ROTATION_270:sx=1-v;sy=u;break;}
      final int px=Math.round(sx*(size.x-1)),py=Math.round(sy*(size.y-1));
      main.removeCallbacksAndMessages(token);
      main.postAtTime(()->{if(!running||generation!=session)return;try{hover.invoke(service,action,px,py);}catch(Exception e){Log.e("SpenPort","Hover callback failed",e);}},token,now);
     }
    }
    input.close();
   }catch(Exception e){if(running&&generation==session)Log.e("SpenPort","Original pen hardware stream failed",e);}
   finally{if(running&&generation==session){running=false;main.post(()->NativeMagnifier.update(service,10,0,0,.5f));}}
  },"SpenRootHover");thread.start();
 }
 public static synchronized void stop(Context c){generation++;running=false;final String tokenValue=streamToken;streamToken=null;if(tokenValue!=null)try{c.getContentResolver().call(android.net.Uri.parse("content://com.samsung.android.service.aircommand.aosp.hardware"),"closePen",tokenValue,null);}catch(RuntimeException e){Log.w("SpenPort","Pen stream close failed",e);}main.removeCallbacksAndMessages(token);if(stream!=null){try{stream.close();}catch(IOException ignored){}stream=null;}thread=null;NativeMagnifier.update(c,10,0,0,.5f);}
}
