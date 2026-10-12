# Capture data labels (1.2.11)

After package UID reassignment, a package root can have the current SELinux MCS
categories while nested cache and database files retain the old categories.
On the development device, Capture obtained the Chrome blank-page screenshot,
but its private temporary image save failed with EACCES. The original editor
received a null image path. WorkManager and shader caches showed the same mismatch.

The authored module helper first runs restorecon on Capture CE/DE data roots,
then validates their current app_data_file context and reconciles descendants
with chcon -hR. Root context is read at runtime; no UID or category is hardcoded.
The helper only targets Capture's user-0 private data, does not delete data,
does not follow external symlink targets and does not alter SELinux policy.
Other users are not currently covered. Enforcing mode remains enabled.

Chrome blank-page Screen write opened the original editor and saved a JPEG into
DCIM/Screenshots after repair. No screenshot pixels were transferred to the PC.
The helper was executed successfully on the device. All three module APK payloads
are byte-identical to installed 1.2.10. The 1.2.11 module update is staged for the
next reboot; live data repair is already applied. On 2026-10-12 the owner confirmed actual-menu Screen write and Smart select
work in third-party apps and save normally. This is not a FLAG_SECURE bypass.
