package org.evox.spen.compat;
/** Samsung-style hover button click; contact belongs to the application. */
public final class PenTapSequence {
 private boolean initialized,previousSide,armed;
 private long pressed,last=-1000,generation;
 public synchronized int[] frame(boolean hover,boolean touch,boolean side,int x,int y,long now,boolean enabled){
  if(!initialized){initialized=true;previousSide=side;return null;}
  // Cancel the entire press, including lift/recontact while the button stays held.
  if(!enabled||!hover||touch){armed=false;generation++;}
  if(side&&!previousSide){armed=enabled&&hover&&!touch;pressed=now;generation++;}
  int[] result=null;
  if(!side&&previousSide){
   long duration=now-pressed;
   if(armed&&enabled&&hover&&!touch&&duration>0&&duration<=800&&now-last>=400&&x>=0&&y>=0){
    last=now;result=new int[]{x,y};
   }
   armed=false;
  }
  previousSide=side;return result;
 }
 public synchronized long ticket(){return generation;}
 public synchronized boolean canDispatch(long ticket){return initialized&&generation==ticket&&!previousSide;}
 public synchronized void reset(){initialized=previousSide=armed=false;generation++;}
}
