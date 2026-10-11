# Permissions and interfaces

Only Air Command requires application Root for daily operation: Samsung hardware nodes, Linux input and task/input operations lacking equivalent public APIs. Capture and Magnify use fixed operations through protected in-package providers and require no application Root. The hardware provider checks allowed package identity and matching signatures; the magnification provider checks signatures and has a package signature permission. No arbitrary external Shell command API is exposed. Disable Shell Root after maintenance.

Magnification requires Air Command's internal accessibility service. Background media control requires notification access for the original notification listener; the user may disable it. This access can expose notification data. The new media adapter matches media sessions and notification package names; this source review is not a full audit of the original Samsung application.

Scripts grant Air Command WRITE_SECURE_SETTINGS and Bluetooth permissions, allow overlays/background operation for the three tools and configure Capture storage appops. They do not automatically enable every accessibility or notification listener, and only migrate the previously enabled magnification service in the existing accessibility list.

Interface layers: Android SDK (WindowManager, MediaSessionManager/MediaController, ContentProvider, accessibility, MediaStore); version-sensitive AOSP hidden/internal interfaces and shell commands; Linux input plus Samsung sec_epen/EFS hardware. Root helpers live inside Air Command. Input node IDs and coordinate ranges target the development tablet, not arbitrary AOSP hardware.

Disable the module and reboot to remove its mounts. Application data and Root profiles are not automatically erased. Separately installed APKs remain after module removal. Do not sign this project with the OS platform signing key.
