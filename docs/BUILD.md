# Local build boundaries

Obtain a JDK and Android SDK yourself (development used android-35/android.jar). Compile the independently authored adapters:

```
python tools/compile_adapters.py --android-jar /path/android.jar --javac /path/javac --output build/classes
```

Compilation groups are separate. This command does not produce a complete application. Reflection/runtime integration still needs device testing.

Build `resources` with aapt2 compile/link and `--package-id 0x7e --allow-reserved-package-id --stable-ids resources/stable-ids.txt`. Accessibility XML, Settings icon and dynamic background have IDs 0x7e010000, 0x7e020000 and 0x7e030000 respectively.

`python tools/add_settings_alias.py SOURCE OUTPUT` adds an AOSP Settings alias to an authorized local Air Command APK without a pre-existing alias. Other input entries are preserved. The unsigned intermediate output must be merged with the resource package and re-signed; it is not a complete port or directly installable result.

A complete port requires an authorized donor, original call-site modifications, resource-table merging and a common project signing identity. Complete proprietary modification payloads are not published. Unmodified stock APKs are not supported inputs to the module template.

If you already have three legally adapted and same-signed local APKs, package a local module:

```
python tools/package_module.py --air AIR.apk --capture CAP.apk --magnify MAG.apk --output build/local-module.zip
```

Signing and runtime compatibility must be verified separately. Do not upload generated modules to public repositories or Releases without relevant redistribution rights. Generate and retain your own private signing keys offline.
