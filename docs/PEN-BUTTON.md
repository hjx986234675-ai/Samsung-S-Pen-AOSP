# Side-button menu arbitration (1.2.11)

The previous adapter treated side-button tip contact as a menu gesture and intercepted contact with a transparent overlay. This conflicted with hold-to-erase in note and drawing applications.

The locally inspected Samsung donor framework's `SpenGestureManagerService.processMotionEventForAirCMD` handles the menu in hover events on button release, with a press duration greater than zero and at most 800ms, a 400ms launch guard, and separate contact tracking. Its receiver implements the system PointerEventListener. Samsung's [user instructions](https://www.samsung.com/ca/support/mobile-devices/use-and-manage-the-air-command-menu-on-your-galaxy-device/) likewise describe hovering and pressing the side button. This repository does not redistribute the donor framework or its decompiled source.

`PenTapSequence` now requires a hover-only press/release. Tip contact cancels that entire press, including lift/recontact while holding the button; leaving proximity, disabling the setting and stream reset also cancel it. Long hover holds are rejected. A subsequent new hover click can open the menu. The 800ms duration limit and 400ms guard follow the inspected donor. `NativePenInput` no longer adds a contact interception window. Queued dispatch is invalidated by contact before execution. Existing BLE/proximity arbitration and the original menu remain connected. There are no package-name exceptions.

The historical `native_pen_tap` extra remains an internal marker for the validated hardware request. It now marks a hover button click rather than a tip tap. Other original menu causes remain unchanged.

Regression checks:

```
javac --release 8 -d build/button-tests adapters/aircommand/PenTapSequence.java tests/PenButtonRegression.java
java -cp build/button-tests PenButtonRegression
```

All 15 checks passed. APK signing/alignment and module ZIP checks passed; only the Air Command code and signing block changed within its APK, and Capture/Magnify payloads were identical to 1.2.10. A same-signed data-preserving Air Command update was installed on SM-T970 running Android 17. Its UID and SYSTEM status were retained, the installed hash matched, and hardware input monitoring restarted. The owner confirmed the reported problem fixed on 2026-10-11. A full module reinstall/reboot and all-feature regression were not repeated for this fix.
