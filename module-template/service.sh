#!/system/bin/sh
MODDIR=${0%/*}
attempt=0
while [ "$(/system/bin/getprop sys.boot_completed)" != 1 ] && [ "$attempt" -lt 90 ]; do
 sleep 2
 attempt=$((attempt + 1))
done
[ "$(/system/bin/getprop sys.boot_completed)" = 1 ] || exit 1
attempt=0
while ! /system/bin/dumpsys user | /system/bin/grep -q '0=RUNNING_UNLOCKED'; do
 [ "$attempt" -ge 180 ] && exit 1
 sleep 2
 attempt=$((attempt + 1))
done
# Pipe Binder-command output: system_server cannot write directly to module-labelled files.
/system/bin/sh "$MODDIR/apply.sh" 2>&1 | /data/adb/ksu/bin/busybox tee "$MODDIR/startup.log"
