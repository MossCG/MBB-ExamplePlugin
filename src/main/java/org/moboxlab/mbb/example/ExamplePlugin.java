package org.moboxlab.mbb.example;

import org.moboxlab.moboxbot.API.Plugin;

/**
 * MBB-ExamplePlugin
 * MoBoxBot 官方插件开发示例。
 */
public class ExamplePlugin extends Plugin {
    private ExampleTask exampleTask;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getLogger().sendInfo("示例插件正在加载，数据目录："+getDataFolder());
        getLogger().sendInfo("plugin.json 大小："+readResource("plugin.json").length+" 字节");
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerListener(this,new ExampleListener(this));
        getServer().getPluginManager().registerCommand(this,new ExampleCommand(this));
        exampleTask = new ExampleTask(this);
        exampleTask.start();
        getLogger().sendInfo("示例插件已启用！");
    }

    @Override
    public void onDisable() {
        if (exampleTask != null) exampleTask.stop();
        getLogger().sendInfo("示例插件已停用！");
    }

    public String getHelloText() {
        return getConfig().getString("helloText","你好，这是 MBB-ExamplePlugin！");
    }
}
