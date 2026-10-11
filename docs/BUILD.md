# 本地构建边界

需要自行取得 JDK、Android SDK（开发使用 android-35/android.jar）及对应工具。`python tools/compile_adapters.py --android-jar /path/android.jar --javac /path/javac --output build/classes` 分组编译自编兼容层，不能输出完整应用。部分接口为反射和运行时适配，编译通过不等于真机可用。

`resources` 可用 aapt2 compile/link 构建独立资源包，参数 `--package-id 0x7e --allow-reserved-package-id --stable-ids resources/stable-ids.txt`。无障碍 XML/图标/颜色依次为 0x7e010000/0x7e020000/0x7e030000。

`tools/add_settings_alias.py SOURCE OUTPUT` 只在尚无入口的授权本地 Air Command 输入 APK 上插入 Settings 别名，保留其其他文件；输出尚未签名且必须合并 `resources` 包。不是完整移植流程，不要直接安装该中间结果。

完整 APK 需要对应合法供体、原版调用点改写、资源表合并和同项目签名；这些专有修改载荷未公开。不要把三个未经适配的原厂 APK 当作可用输入。

若已经拥有合法完成适配且同签名的三个 APK，可用 `python tools/package_module.py --air AIR.apk --capture CAP.apk --magnify MAG.apk --output build/local-module.zip` 生成仅供本地的模块 ZIP。不要将该生成文件上传到公开仓库或 Releases，除非取得相关再分发权利。签名密钥自行生成并离线保管。
