# Samsung S Pen AOSP adapters

Original compatibility adapters developed to restore S Pen features on Samsung Galaxy Tab S7+ (SM-T970 / gts7xlwifi) running AOSP. The local port reuses Samsung Air Command, Samsung Capture and Magnify interfaces and recognition logic, with adapters inside those packages.

**This repository contains source and module templates only. It does not include Samsung APKs, decompiled Samsung code, proprietary resources/models, signing keys or an installable full module. It cannot currently rebuild the full port from stock APKs: complete modifications to the proprietary call sites are not distributed.** This is an independent project, not a Samsung release.

## Included components

| Directory | Purpose |
| --- | --- |
| `adapters/aircommand` | Framework compatibility, native pen/BLE access, existing Air Actions rules, background media sessions, original menu integration, tip position/input reservation, docking/multiple-pen policy, hover pointer, left-behind reminder and internal magnification service |
| `adapters/capture` | Compatibility for original Smart select/Screen write startup, capture, storage and Android sharing |
| `adapters/magnify` | Hover input and magnification integration for the original Magnify tool |
| `resources` | Authored accessibility XML, monochrome Settings pen icon and light/dark dynamic colors in resource package `0x7e` |
| `module-template` | Three-app system mounting, startup, permissions, Settings entry and uninstall scripts, without APK payloads |
| `tools` | Adapter compilation, Settings alias insertion, local packaging of already adapted APKs and public-tree checks |
| `docs` | Changes to original applications, permissions, build boundaries and release audit |

## Tested scope

Development device: SM-T970, EvolutionX Android 17/API 37, SukiSU/KernelSU. The adapter audit found no EvoX/Lineage-specific framework API dependency. `org.evox.spen` is a historical namespace. AOSP hidden interfaces, Root, Samsung sec_epen/EFS hardware and a particular Samsung application layout are still required. Android 15/16, other ROMs, the smaller Tab S7 and other hardware have not been verified.

The owner tested the main features over several iterations; this is not a complete compatibility certification. Version 1.2.6 adds the Settings icon/dynamic color fix and English text for authored UI additions. Local compilation, resource, signing and alignment checks pass; device UI validation awaits restoration of the USB connection. Reboot or extended undocked idle may still require redocking for Bluetooth. Samsung Notes is not ported; S Pen unlock is hidden. Independent AI select sharing regression and the final full clean-reinstall acceptance remain incomplete.

## Language and research

Authored UI additions use English only. Existing Samsung UI follows its own resources and the device language; this project does not change the system language or strip Samsung translations.

Read [original application changes](docs/CHANGES-TO-SAMSUNG.md), [permissions](docs/PERMISSIONS.md), [release audit](docs/RELEASE-AUDIT.md) and [local build boundaries](docs/BUILD.md). Keep the original software's rights notices. Noncommercial distribution, attribution or device ownership does not grant permission to redistribute proprietary software.

The MIT license applies only to original material in this repository and grants no rights to Samsung or third-party components.
