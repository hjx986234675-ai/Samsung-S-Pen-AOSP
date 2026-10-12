#!/system/bin/sh
MODDIR=${0%/*}
AIR=com.samsung.android.service.aircommand
CAP=com.samsung.android.app.smartcapture
MAG=com.samsung.android.app.readingglass
for pkg in "$AIR" "$CAP" "$MAG"; do
 /system/bin/pm path "$pkg" >/dev/null || exit 1
done
/system/bin/sh "$MODDIR/reconcile-capture-data.sh"
# Keep mappings and Root profiles. Migrate only the previously enabled magnification support service.
OLD=org.evox.spen.bridge/org.evox.spen.bridge.MagnifierService
NEW=$AIR/org.evox.spen.compat.NativeMagnificationService
SERVICES=$(/system/bin/settings get secure enabled_accessibility_services)
case ":$SERVICES:" in
 *":$OLD:"*)
  SERVICES=$(printf "%s" "$SERVICES" | /system/bin/sed "s|$OLD|$NEW|g")
  /system/bin/settings put secure enabled_accessibility_services "$SERVICES"
  ;;
esac
/system/bin/pm grant "$AIR" android.permission.WRITE_SECURE_SETTINGS
for permission in BLUETOOTH_CONNECT BLUETOOTH_SCAN BLUETOOTH_ADVERTISE; do
 /system/bin/pm grant "$AIR" "android.permission.$permission"
done
for pkg in "$AIR" "$CAP" "$MAG"; do
 /system/bin/cmd appops set "$pkg" SYSTEM_ALERT_WINDOW allow
 /system/bin/cmd appops set "$pkg" RUN_ANY_IN_BACKGROUND allow
done
/system/bin/cmd appops set "$CAP" MANAGE_EXTERNAL_STORAGE allow
/system/bin/cmd appops set "$AIR" WRITE_SETTINGS allow
/system/bin/cmd appops set "$MAG" WRITE_SETTINGS allow
/system/bin/pm disable "$AIR/com.samsung.android.service.aircommand.ui.settings.unlock.UnlockConfirmationActivity"
/system/bin/pm disable "$AIR/com.samsung.android.service.aircommand.svc.remotespentrustagent.RemoteSpenTrustAgentService"
# AOSP temporary exemption permits boot service startup without opening a UI.
/system/bin/cmd deviceidle tempwhitelist -d 15000 "$AIR"
/system/bin/am startservice -n "$AIR/com.samsung.android.service.aircommand.svc.aircommandui.AirCommandUiService"
/system/bin/am startservice -n "$AIR/com.samsung.android.service.aircommand.remotespen.RemoteSpenService" -a com.samsung.android.service.aircommand.action.REMOTE_SPEN_SERVICE
# Record whether early mounting actually promoted the package; do not pretend a data-only install is system integration.
/system/bin/dumpsys package "$AIR" | /system/bin/grep 'pkgFlags='
