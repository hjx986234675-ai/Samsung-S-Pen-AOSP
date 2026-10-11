package org.evox.spen.compat;
/** One menu request per side-button tap, after release; independent of Android input delivery. */
public final class PenTapSequence {
 private boolean previous,armed;private long last=-1000;private int x,y;
 public int[] frame(boolean touch,boolean side,int px,int py,long now,boolean enabled){
  if(touch&&!previous)armed=side&&enabled;
  if(touch&&armed){x=px;y=py;}
  int[] result=null;if(!touch&&previous&&armed&&enabled&&x>=0&&y>=0&&now-last>700){last=now;result=new int[]{x,y};}
  if(!touch)armed=false;previous=touch;return result;
 }
 public void reset(){previous=armed=false;}
}
