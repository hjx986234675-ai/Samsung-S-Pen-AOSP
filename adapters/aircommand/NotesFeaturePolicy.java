package org.evox.spen.compat;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/** Remove the two excluded Notes gestures from the original preference hierarchy. */
public final class NotesFeaturePolicy {
    public static void hide(Object fragment) {
        try {
            Object manager = fragment.getClass().getField("i").get(fragment);
            Object screen = manager.getClass().getField("g").get(manager);
            if (screen != null) remove(screen);
        } catch (ReflectiveOperationException failure) {
            Log.e("AirCommandPort", "Excluded Notes preferences could not be removed", failure);
        }
    }
    private static void remove(Object group) throws ReflectiveOperationException {
        Class<?> groupClass = Class.forName("androidx.preference.PreferenceGroup");
        Class<?> preferenceClass = Class.forName("androidx.preference.Preference");
        List<?> children = new ArrayList<>((List<?>)groupClass.getField("g0").get(group));
        for (Object preference : children) {
            String key = (String)preferenceClass.getField("s").get(preference);
            if ("key_create_note_with_pen_button".equals(key) || "key_screen_off_note".equals(key)) {
                groupClass.getMethod("J", preferenceClass).invoke(group, preference);
                Log.i("AirCommandPort", "Excluded Notes preference removed: " + key);
            } else if (groupClass.isInstance(preference)) remove(preference);
        }
    }
}
