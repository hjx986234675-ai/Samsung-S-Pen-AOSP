# 权限与接口

日常仅 Air Command 需要应用 Root，用于 Samsung 硬件节点、输入事件和 AOSP 无公开等价接口的任务/输入操作。Capture 和 Magnify 经包内 provider 使用固定操作，不需要 Root。provider 具有包权限及同签名/调用包校验，不提供外部任意 Shell 命令执行。Shell Root 只用于维护，完成安装后关闭。

放大需要 Air Command 包内无障碍服务；后台媒体需要原版通知监听服务的通知使用权，用户可关闭。通知权限可以接触通知信息，新增媒体代码仅匹配媒体会话与通知包名；原版软件自身行为不因本次源码扫描得到全面审计。

脚本向 Air Command 授予 WRITE_SECURE_SETTINGS、蓝牙权限；为三个工具启用悬浮窗/后台运行，Capture 使用存储 appop。没有自动打开所有通知监听或无障碍服务，不修改其他服务列表，只迁移已启用的旧放大服务。

接口分层：Android SDK（WindowManager、MediaSessionManager、MediaController、ContentProvider、无障碍、MediaStore 等）；AOSP 隐藏/内部接口及 shell 命令（版本敏感）；Linux 输入和 Samsung sec_epen/EFS（硬件相关）。Root 命令桥位于原 Air Command 包内。硬件事件节点及坐标范围针对开发机，不能宣传为通用 AOSP 设备适配。

禁用模块并重启可移除挂载，应用数据/Root 配置不自动清空。原有单独安装的 APK 不因卸载模块自动消失。不要用系统平台签名签署本项目。
