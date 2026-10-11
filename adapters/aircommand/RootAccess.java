package org.evox.spen.compat;
import java.io.*;import java.util.concurrent.TimeUnit;
public final class RootAccess {
 public static String quote(String s){return "'"+s.replace("'","'\\''")+"'";}
 public static String run(String command)throws IOException,InterruptedException{
  Process p=new ProcessBuilder("su","-c",command).redirectErrorStream(true).start();
  if(!p.waitFor(4,TimeUnit.SECONDS)){p.destroy();throw new IOException("Root command timeout");}
  if(p.exitValue()!=0)throw new IOException("Root command denied or failed");StringBuilder s=new StringBuilder();try(BufferedReader in=new BufferedReader(new InputStreamReader(p.getInputStream()))){String row;while((row=in.readLine())!=null)s.append(row).append('\n');}return s.toString();
 }
}
