# Hover guide input region (1.2.8)

The original Air Actions battery/action guide can interrupt hover on its own
floating icon when Samsung window extensions are unavailable. The original
hover-exit handler then dismisses the guide, and the icon receives hover again.

GuidePanelInputRegion uses AOSP internal-insets input regions. Before the
original guide is attached, install its listener. When the floating icon
receives hover-enter, remember that View. The guide input region subtracts
that icon's screen-space bounds, translated into guide-local coordinates.
The icon remains the hover target while the other guide controls accept input.
Showing, content, actions and dismissal continue through the original logic.

This uses AOSP hidden reflection interfaces already needed by the port, not
an EvolutionX-specific API. Reflection failures are logged explicitly.
Retained Samsung call-site changes are local and excluded from this repository.

Validation: local compile, signature and alignment checks passed.
Physical hover stability and control interaction are pending confirmation.
The full local module includes Samsung APKs and is not published here.
