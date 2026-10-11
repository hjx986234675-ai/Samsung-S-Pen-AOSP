# Samsung S Pen AOSP adapters

为 Samsung Galaxy Tab S7+（SM-T970 / gts7xlwifi）在 AOSP 系统中恢复 S Pen 使用体验的适配源码。使用原版 Samsung Air Command、Samsung Capture、Magnify 的界面和识别逻辑，在各包内部接入 Android/Linux 能力。

这是自编兼容层和模块模板的源码仓库。**不提供三星 APK、反编译源码、专有资源、模型、签名密钥或可直接刷入的完整模块。当前公开材料不能从原厂 APK 一键重建完整移植；原版调用点的完整修改载荷没有公开。** 不以源码发布代替专有软件的再分发授权。不是三星官方项目。

## 提供的部分

| 部分 | 工作内容 |
| --- | --- |
| `adapters/aircommand` | 缺失框架接口兼容、原版蓝牙服务硬件访问、隔空手势与前台应用规则、标准后台媒体会话、原版悬浮菜单、笔尖位置和触控接管、回槽/多笔浮窗、指针、遗落提醒、包内放大无障碍支持 |
| `adapters/capture` | 原版智能选择与截屏编写的启动、画面获取、存储和系统分享接口兼容 |
| `adapters/magnify` | 原版放大镜悬停输入与包内放大服务接入 |
| `resources` | 自编无障碍 XML、设置笔形矢量图标、浅深色动态色资源；独立资源包 `0x7e` |
| `module-template` | 三应用系统挂载、启动恢复、设置入口、权限配置和卸载脚本；没有二进制载荷 |
| `tools` | 适配源码编译、AOSP 设置入口插入、使用已合法适配/签名的本地 APK 打包、发布范围检查 |
| `docs` | 权限、接口、原版改动范围、验证状态、版权与社区发布边界 |

## 已验证范围

开发机为 SM-T970，EvolutionX Android 17/API 37，SukiSU/KernelSU。新增代码没有 EvoX/Lineage 专用框架 API 依赖；`org.evox.spen` 只是历史命名空间。仍依赖 AOSP 隐藏接口、Root、Samsung sec_epen/EFS 驱动与原版包的具体调用布局。**没有验证 Android 15/16、其他 ROM、Tab S7 普通版或其他设备。**

功能由机主多轮测试确认可用；这不是全面兼容性认证。最新设置外观修正 1.2.6 已通过本地资源/签名/对齐检查，真机外观尚待确认。开机或长期离槽后蓝牙仍可能需要回槽。Samsung Notes 未移植；S Pen unlock 隐藏；独立 AI select 分享回归尚未完成。

## 使用与研究

阅读 [原版改动说明](docs/CHANGES-TO-SAMSUNG.md)、[权限](docs/PERMISSIONS.md)、[发布审查](docs/RELEASE-AUDIT.md) 和 [本地构建](docs/BUILD.md)。保留三星原版程序的所有权利说明。完整应用的再分发需要对应权利；非商业、注明出处或要求用户拥有设备均不自动取得授权。

许可证仅适用于本仓库自编代码/文档/图标；不授予 Samsung 或第三方组件的任何权利。
