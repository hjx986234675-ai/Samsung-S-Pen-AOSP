# Changes to original applications

The local port modifies and re-signs the original APKs. It is not an unmodified installation of Samsung applications.

Air Command: merge original compatibility classes into the application; redirect missing Samsung framework calls to Android SDK, AOSP internal interfaces or fixed Root operations; connect Samsung sec_epen insertion/charging and EFS pen identity; adapt the original BLE connection flow; retain Samsung recognition and gesture configuration. Connect the original notification monitor and rule selection to Android media sessions. Adapted foreground-app rules take precedence over background-media and generic rules. Retain original menu UI; add pen position and button/gesture arbitration. Version 1.2.11 follows hover-only side-button release for menu requests and leaves tip contact available to applications; the earlier temporary touch reservation was removed. Merge magnification support inside Air Command. Expose original S Pen settings through AOSP EXTRA_SETTINGS/IA_SETTINGS and hide unused unlock entry points. Add a separate resource package without replacing the original Samsung resource package.

Samsung Capture: adapt startup, screen acquisition, storage, sharing and optional Knox/framework calls while retaining original selection and drawing UI. Obtain screenshots through the same-signed Air Command protected provider; use Android media storage and the original sharing workflow with the system chooser.

Magnify: adapt hover input and magnification calls to Air Command's internal support service. Retain the original toolbar, scale controls and close action.

All three modified APKs use the same local project signing identity, not Samsung's original or the OS platform signature. Mount as regular system/app packages. No Settings.apk/framework.jar replacement, ROM partition write or SELinux permissive setting. Startup scripts configure specified application permissions and restore services.

These changes create modified proprietary application versions. Free distribution or an MIT license on adapters does not authorize distribution of the complete port. Full modified Samsung smali, original resources and APKs are not included here.

For Android 16+ Expressive Settings, the alias declares `com.android.settings.icon_color_scheme=green` and `com.android.settings.group_key=top_level_support_category`. These are SettingsLib metadata, not EvoX APIs. The target group was checked in the actual device Settings resource XML; other Settings implementations may ignore the color hint or lack that group and fall back to the root page. Older tint/background hints remain. [AOSP Android 16 TileUtils](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/android16-release/packages/SettingsLib/Tile/src/com/android/settingslib/drawer/TileUtils.java) documents the metadata.
