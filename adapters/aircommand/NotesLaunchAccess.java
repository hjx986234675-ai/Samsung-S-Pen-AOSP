package org.evox.spen.compat;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.UserHandle;

/** Same-user shortcuts need no privileged cross-user activity API. */
public final class NotesLaunchAccess {
    public static void activity(Context context, Intent original, UserHandle user) {
        Parcel parcel = Parcel.obtain();
        int id;
        try { user.writeToParcel(parcel, 0); parcel.setDataPosition(0); id = parcel.readInt(); }
        finally { parcel.recycle(); }
        if (id >= 0 && id != UserAccess.myId())
            throw new SecurityException("Cross-user shortcut launch is unavailable");
        context.startActivity(new Intent(original).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
