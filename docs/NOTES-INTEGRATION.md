# Optional Notes menu integration (1.2.13)

Notes keeps its independent APK, module, data and editor. Air Command can invoke
its original exported PopupNoteService for Create note. The S Pen module enables
the overlay permission needed by that popup when Notes is installed. Notes does
not need application Root and is not bundled into the S Pen module.

View all notes used privileged startActivityAsUser reflection and failed on AOSP.
The local call site calls NotesLaunchAccess.activity, which verifies the current
user and uses standard Context.startActivity with the original component/action.

At the owner's request, Quick notes and Screen off memos are excluded. Native
contact/docking callbacks, the setting observer and driver synchronization from
the experimental integration were removed. NotesFeaturePolicy removes the two
preferences from the original hierarchy. Their settings read as disabled and
writes cannot enable them. Module startup clears old stored values and disables
the previously enabled driver option if its node exists. No other AOD/display
flags are modified. The experimental Notes screen-off executor was reverted to
the pre-experiment 0.2.8 APK, without clearing notes data.

Build/signature/alignment checks passed. Runtime logs confirmed removal of both
preferences. Create note previously passed after overlay permission was granted.
On 2026-10-12 the owner confirmed menu expansion, Create note and View all notes work after removal. Updates are staged
for the next reboot; application updates and live settings repair are applied.
No Samsung APKs or retained Samsung call-site code are published here.
