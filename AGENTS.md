# AGENTS.md

## 前置约束

- 本仓库是 MoBoxBot 官方插件开发示例。
- 只依赖 `org.moboxlab.moboxbot.API`，不能访问主程序内部类。
- Java 8。
- 中文注释、中文日志；不用 emoji。
- 示例必须保持可编译、可安装、可重载。

## 示例边界

示例需要覆盖：

- 生命周期
- 配置
- 命令与权限
- 事件
- OneBot 消息
- 图片渲染
- 存储
- 定时任务
- 服务门面

示例不追求业务完整度，优先保持接口调用清晰。

## 构建

```powershell
.\build.ps1
```

默认读取：

```text
..\MoBoxBot\out\MoBoxBot.jar
```

## 版本与提交

- 版本格式：`V大版本.小版本.小更新.小修正.四位时间戳`。
- 功能更新同步 `plugin.json`、`version.txt` 和 `update.md`。
- 纯文档调整使用 `docs: 中文摘要`。
- 不提交 `out/`、日志、数据库、Token 和个人信息。

## 交付前自检

1. `build.ps1` 通过。
2. JAR 根目录包含 `plugin.json`。
3. 主类继承 `API.Plugin`。
4. 示例覆盖的 API 调用与 `API.md` 一致。
5. 插件停用和重载后没有旧监听器继续响应。
