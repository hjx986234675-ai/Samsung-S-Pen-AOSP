#!/system/bin/sh
MODDIR=${0%/*}
# Narrow mount for our three app directories only; existing system apps are lower layer.
# A metamodule may provide this mount instead. Never overlay Settings/framework partitions.
[ -f /system/app/SamsungAirCommand/SamsungAirCommand.apk ] && exit 0
/data/adb/ksu/bin/busybox mount -t overlay -o "lowerdir=$MODDIR/system/app:/system/app" KSU /system/app
