package com.samsung.android.widget;
import android.app.KeyguardManager;import android.content.Context;
/** EvoX has no Samsung FMM/carrier lock services; retain a conservative Android keyguard guard. */
public final class SemLockPatternUtils {
 private final KeyguardManager keyguard;
 public SemLockPatternUtils(Context c){keyguard=(KeyguardManager)c.getSystemService(Context.KEYGUARD_SERVICE);}
 private boolean locked(int user){if(user>=0&&user!=org.evox.spen.compat.UserAccess.myId())throw new SecurityException("Cross-user lock query unavailable");return keyguard!=null&&keyguard.isDeviceLocked();}
 public boolean isFmmLockEnabled(int user){return locked(user);}public boolean isRemoteMobileManagerLockEnabled(int user){return locked(user);}public boolean isCarrierLockEnabled(int user){return locked(user);}
}
