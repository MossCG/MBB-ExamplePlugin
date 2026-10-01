# 参与 MBB-ExamplePlugin 开发

## 环境

- Java 8
- PowerShell 5.1 或 PowerShell 7
- MoBoxBot 主程序 JAR

## 构建主程序

```powershell
cd D:\CodeX\Projects\MoBoxBot
.\build.ps1
```

## 构建示例插件

```powershell
.\build.ps1 -Bot "D:\CodeX\Projects\MoBoxBot\out\MoBoxBot.jar"
```

## 代码规范

- Java 8，不使用 `var`、stream、Lombok。
- 插件只依赖 `API` 包。
- 中文注释、中文日志。
- 插件资源读取使用 `readResource` / `readResourceText`。
- 插件注册资源必须交给主程序记账。

## 提交规则

1. 功能更新同步 `plugin.json`、`version.txt` 与 `update.md`。
2. 纯文档调整使用 `docs: 中文摘要`。
3. 推送 `master` 后 GitHub Actions 自动构建并发布。
4. 不提交构建产物和真实凭据。

## 自检

1. `build.ps1` 通过。
2. 插件可加载、启用、停用和重载。
3. 命令权限、冷却和说明完整。
4. 示例变更与主程序 `API.md` 一致。
