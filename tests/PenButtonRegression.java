import org.evox.spen.compat.PenTapSequence;
public final class PenButtonRegression {
 static int checks;
 static void check(boolean b,String name){checks++;if(!b)throw new AssertionError(name);}
 static PenTapSequence start(){PenTapSequence s=new PenTapSequence();s.frame(true,false,false,10,20,1000,true);return s;}
 static int[] f(PenTapSequence s,boolean hover,boolean touch,boolean side,long t){return s.frame(hover,touch,side,10,20,t,true);}
 public static void main(String[] args){
  PenTapSequence s=start();f(s,true,false,true,1010);check(f(s,true,false,false,1100)!=null,"hover click opens");
  s=start();f(s,true,false,true,1010);f(s,true,true,true,1050);f(s,true,false,true,1100);check(f(s,true,false,false,1150)==null,"eraser lift then release never opens");
  s=start();f(s,true,true,true,1010);check(f(s,true,false,false,1100)==null,"button down on contact never opens");
  s=start();f(s,true,false,true,1010);f(s,true,true,false,1050);check(f(s,true,false,false,1100)==null,"button release on contact never opens");
  s=start();f(s,true,false,true,1010);check(f(s,true,false,false,1811)==null,"long hold never opens");
  s=start();f(s,true,false,true,1010);check(f(s,true,false,false,1810)!=null,"Samsung 800ms boundary");
  s=start();f(s,true,false,true,1010);f(s,false,false,true,1050);f(s,true,false,true,1100);check(f(s,true,false,false,1150)==null,"proximity loss cancels whole press");
  s=start();f(s,true,false,true,1010);s.frame(true,false,true,10,20,1050,false);check(f(s,true,false,false,1100)==null,"setting change cancels");
  s=start();f(s,true,false,true,1010);f(s,true,false,false,1100);long ticket=s.ticket();check(s.canDispatch(ticket),"valid queued launch");f(s,true,true,false,1101);check(!s.canDispatch(ticket),"contact cancels queued launch");
  s=start();f(s,true,false,true,1010);s.reset();check(f(s,true,false,false,1100)==null,"reset cancels");
  s=new PenTapSequence();f(s,true,false,true,1010);check(f(s,true,false,false,1100)==null,"startup with held button does not arm");
  s=start();f(s,true,false,true,1010);f(s,true,false,false,1100);f(s,true,false,true,1200);check(f(s,true,false,false,1300)==null,"400ms debounce");
  s=start();f(s,true,false,true,1010);f(s,true,true,true,1050);f(s,true,false,true,1100);f(s,true,true,true,1150);f(s,true,false,true,1200);check(f(s,true,false,false,1250)==null,"multiple eraser strokes stay cancelled");
  s=start();f(s,true,false,true,1010);s.frame(true,true,true,10,20,1050,true);f(s,true,false,false,1100);f(s,true,false,true,1200);check(f(s,true,false,false,1300)!=null,"new hover click works after erasing");
  System.out.println("PASS "+checks+" gesture regression checks");
 }
}
