#!/system/bin/sh
# Reconcile stale SELinux app-data categories after package UID reassignment.
# Android's current package-root label is authoritative; never hardcode MCS IDs.
CAP=com.samsung.android.app.smartcapture
status=0
for base in /data/user/0 /data/user_de/0; do
 dir="$base/$CAP"
 [ -d "$dir" ] || continue
 /system/bin/restorecon -RF "$dir" || status=1
 context=$(/system/bin/stat -c %C "$dir")
 if ! printf '%s\n' "$context" | /system/bin/grep -Eq '^u:object_r:app_data_file:s0:c[0-9]+(,c[0-9]+)*$'; then
  echo "Capture data reconciliation skipped: unexpected root label for $dir"
  status=1
  continue
 fi
 # -h keeps symlinks from relabeling their external targets.
 /system/bin/chcon -hR "$context" "$dir" || status=1
 echo "Capture data label reconciled: $dir"
done
exit "$status"
