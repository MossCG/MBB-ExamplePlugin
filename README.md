# MBB-ExamplePlugin

MoBoxBot 官方插件开发示例。

本仓库对应插件 API `0.1`，演示推荐的项目结构、生命周期、配置、命令、事件、消息发送、图片渲染、存储和定时任务用法。

主程序仓库：

```text
https://github.com/MossCG/MoBoxBot
```

API 文档：

```text
https://github.com/MossCG/MoBoxBot/blob/master/API.md
```

## 演示内容

- `plugin.json` 与插件生命周期
- `config.yml` 默认释放、读取与写回
- 聊天命令、别名、权限和冷却
- 群消息、私聊消息、通知、请求、元事件和原始事件
- 文本、图片、@、回复和组合消息
- `ImageUtil` PNG 图片渲染
- `StorageService` 键值存储
- `runTask` / `runTaskLater` / `runTaskTimer`
- `Server` 服务门面与管理员、插件信息查询

## 构建

默认读取同级 `MoBoxBot` 的构建产物：

```powershell
.\build.ps1
```

也可以显式指定：

```powershell
.\build.ps1 -Bot "D:\CodeX\Projects\MoBoxBot\out\MoBoxBot.jar"
```

产物：

```text
out\MBB-ExamplePlugin.jar
```

## 安装

把 `MBB-ExamplePlugin.jar` 放入：

```text
MoBoxBot\plugins\
```

开发模式可以执行：

```text
plugin reload MBB-ExamplePlugin
```

生产环境更新插件建议重启进程。

## 示例命令

命令前缀由主程序配置决定，默认是 `/`。

| 命令 | 权限 | 说明 |
|---|---|---|
| `/example` | `EVERYONE` | 显示示例命令列表 |
| `/example info` | `EVERYONE` | 显示插件与服务信息 |
| `/example image` | `EVERYONE` | 渲染并发送图片 |
| `/example counter` | `EVERYONE` | 演示插件键值存储 |
| `/example message` | `EVERYONE` | 演示组合消息发送 |
| `/example config` | `EVERYONE` | 查看插件配置 |
| `/example admins` | `BOT_ADMIN` | 查看机器人管理员 |
| `/example plugins` | `OWNER` | 查看插件列表 |
| `/example task [秒数]` | `BOT_ADMIN` | 演示延迟任务 |
| `/example config set <文本>` | `BOT_ADMIN` | 写入并保存配置 |

群消息内容为 `example-reply` 时，监听器会演示回复消息段。

## 目录结构

```text
MBB-ExamplePlugin/
├─ build.ps1
├─ build.bat
├─ src/main/java/org/moboxlab/mbb/example/
│  ├─ ExamplePlugin.java
│  ├─ ExampleCommand.java
│  ├─ ExampleListener.java
│  └─ ExampleTask.java
└─ src/main/resources/
   ├─ plugin.json
   └─ config.yml
```

## 开发约定

1. 插件只依赖 `org.moboxlab.moboxbot.API`。
2. 插件 JAR 不打包 MoBoxLib、fastjson、sqlite-jdbc、Java-WebSocket。
3. 插件自己创建的资源必须在 `onDisable` 中释放。
4. 注册监听器、命令和任务时必须传入当前插件实例。
5. 插件 SQL 只允许访问 `plugin_` 前缀表。

## 发布

推送 `master` 后，GitHub Actions 会：

1. 检出并构建 MoBoxBot。
2. 构建 `MBB-ExamplePlugin.jar`。
3. 自动创建或更新 Release。

## 文档

- [CONTRIBUTING.md](CONTRIBUTING.md)：开发与提交规范
- [AGENTS.md](AGENTS.md)：协作约束
- [update.md](update.md)：更新日志
- [SECURITY.md](SECURITY.md)：安全策略

## 许可证

Apache License 2.0，见 [LICENSE](LICENSE)。
