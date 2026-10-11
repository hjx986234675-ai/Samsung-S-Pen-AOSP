# 对原版应用的改动范围

本移植确实修改原版 APK，不是把未经修改的应用直接安装到 AOSP。

Air Command：在原包内合并自编兼容类；将缺失 Samsung 框架调用转接到 Android SDK、AOSP 内部接口或固定 Root 操作；接入 Samsung sec_epen 插入/充电状态与 EFS 笔地址；调整原版 BLE 连接流程；保留原版识别器和手势配置；将通知监听、后台媒体会话和原版规则选择连接；前台已适配应用规则优先于媒体及通用规则。菜单使用原版 UI，新增笔尖位置事件、按键/手势仲裁和临时触控窗口，避免点穿底下应用。包内合并放大支持，设置别名通过 AOSP EXTRA_SETTINGS/IA_SETTINGS 暴露，隐藏未使用的 unlock 入口。新增资源包不替换原版资源包。

Samsung Capture：修改启动/画面获取/存储/分享及可选 Knox 等框架调用点；原版智能选择和绘图 UI 保留。截图通过同签名 Air Command 的受保护 provider 获取；保存后使用 Android 媒体存储，分享使用原版流程接入标准系统选择器。

Magnify：修改悬停输入和放大后端调用点，连接 Air Command 包内服务；原版工具栏、倍率和关闭按钮保留。

三个修改后的 APK 使用同一项目本地签名重新签名，不是三星原厂签名或系统平台签名。系统挂载到普通 system/app，未替换 Settings.apk/framework.jar，未向 ROM 分区写入，未设置 SELinux permissive。模块启动脚本配置指定应用权限并恢复服务。

这些改动涉及原版程序的修改和衍生版本，不能因为免费或源码 MIT 就认为完整移植包得到分发许可。公开仓库不含原版调用点的完整 smali、原版资源和 APK。
