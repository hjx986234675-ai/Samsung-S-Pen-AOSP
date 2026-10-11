# Draft: S Pen compatibility research on Tab S7+ running AOSP

I have been adapting Samsung S Pen features on an SM-T970 running Android 17 AOSP. The local prototype retains Samsung's original UI and recognizer for Air Command/Air Actions, Magnify and Samsung Capture, with compatibility implementations inside those packages.

The source repository shares my compatibility code, authored resources, module templates and an explanation of the changes: https://github.com/hjx986234675-ai/Samsung-S-Pen-AOSP

It contains no Samsung APKs, proprietary resources/models, full decompiled code or signing keys, and cannot currently produce a complete installable port from stock APKs. This is a source/research release, not an official Samsung release or a downloadable universal module.

Testing has been limited to SM-T970, EvolutionX Android 17 and SukiSU/KernelSU. Only Air Command needs daily application Root; internal magnification needs accessibility access and automatic background media control needs notification access. Reboot/long undocked idle may still require redocking to reconnect. Samsung Notes and S Pen unlock are not provided. The latest Settings theme fix awaits device appearance validation after a USB cable failure.

Feedback on the independently authored code and interface compatibility is welcome. No proprietary binary downloads are provided.
