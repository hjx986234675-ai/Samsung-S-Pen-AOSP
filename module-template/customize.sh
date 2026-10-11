ui_print "Samsung S Pen for AOSP - 1.2.6"
[ "$BOOTMODE" = true ] || abort "Install from the running Root manager, not recovery"
[ "$ARCH" = arm64 ] || abort "Original Samsung libraries require arm64"
[ "$API" -ge 35 ] || abort "This build requires Android 15 or newer; tested Android 17"
ui_print "Tested hardware: SM-T970/gts7xlwifi. Other hardware is unverified."
ui_print "System mounting support is required; scripts check the resulting system-app flag."
ui_print "Existing application data and gesture mappings are retained."
set_perm_recursive "$MODPATH" 0 0 0755 0644
set_perm "$MODPATH/post-fs-data.sh" 0 0 0755
set_perm "$MODPATH/service.sh" 0 0 0755
set_perm "$MODPATH/action.sh" 0 0 0755
set_perm "$MODPATH/uninstall.sh" 0 0 0755
