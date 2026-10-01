package org.moboxlab.mbb.example;

import org.moboxlab.moboxbot.API.Plugin;

/**
 * 定时任务示例
 */
public class ExampleTask {
    private final Plugin plugin;
    private boolean stopped = false;
    private int tickCount = 0;

    public ExampleTask(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        int delay = plugin.getConfig().getInt("taskDelaySeconds",3);
        int period = plugin.getConfig().getInt("taskPeriodSeconds",60);
        if (delay < 1) delay = 1;
        if (period < 5) period = 5;

        plugin.getServer().getPluginManager().runTask(plugin,() ->
                plugin.getLogger().sendInfo("runTask 示例任务已执行！"));
        plugin.getServer().getPluginManager().runTaskLater(plugin,() ->
                plugin.getLogger().sendInfo("runTaskLater 示例任务已执行！"),delay);
        plugin.getServer().getPluginManager().runTaskTimer(plugin,this::tick,period,period);
    }

    public void stop() {
        stopped = true;
        plugin.getLogger().sendInfo("示例定时任务已停止！");
    }

    private void tick() {
        if (stopped) return;
        tickCount++;
        if (plugin.getConfig().getBoolean("eventLog",false)) {
            plugin.getLogger().sendInfo("runTaskTimer 示例执行次数："+tickCount);
        }
    }
}
