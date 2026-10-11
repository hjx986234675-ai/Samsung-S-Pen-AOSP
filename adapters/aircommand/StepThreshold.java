package org.evox.spen.compat;

/** Handles detector events and absolute step-counter baselines without phantom steps. */
public final class StepThreshold {
 private final int target;private float baseline=Float.NaN;private float steps;private boolean fired;
 public StepThreshold(int target){if(target<1)throw new IllegalArgumentException("steps");this.target=target;}
 public boolean sample(boolean counter,float value){
  if(fired || Float.isNaN(value) || Float.isInfinite(value) || value<0)return false;
  if(counter){if(Float.isNaN(baseline) || value<baseline){baseline=value;steps=0;return false;}steps=value-baseline;}
  else steps+=value;
  if(steps<target)return false;fired=true;return true;
 }
}
