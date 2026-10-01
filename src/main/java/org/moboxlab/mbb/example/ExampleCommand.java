package org.moboxlab.mbb.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.moboxlab.moboxbot.API.Command.BotCommand;
import org.moboxlab.moboxbot.API.Command.CommandPermission;
import org.moboxlab.moboxbot.API.Command.CommandSender;
import org.moboxlab.moboxbot.API.OneBot.MessageUtil;
import org.moboxlab.moboxbot.API.PluginInfo;
import org.moboxlab.moboxbot.API.Util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例命令
 */
public class ExampleCommand extends BotCommand {
    private final ExamplePlugin plugin;

    public ExampleCommand(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("example");
        prefixList.add("mbbexample");
        return prefixList;
    }

    @Override
    public CommandPermission permission() {
        return CommandPermission.EVERYONE;
    }

    @Override
    public int cooldownSeconds() {
        return 2;
    }

    @Override
    public String description() {
        return "官方插件开发示例";
    }

    @Override
    public boolean execute(CommandSender sender,String[] args) {
        String action = args.length > 1 ? args[1].toLowerCase() : "help";
        if ("info".equals(action)) {
            sendInfo(sender);
            return true;
        }
        if ("image".equals(action)) {
            sendImage(sender);
            return true;
        }
        if ("counter".equals(action)) {
            sendCounter(sender);
            return true;
        }
        if ("message".equals(action)) {
            sendMessage(sender);
            return true;
        }
        if ("config".equals(action)) {
            handleConfig(sender,args);
            return true;
        }
        if ("admins".equals(action)) {
            sendAdmins(sender);
            return true;
        }
        if ("plugins".equals(action)) {
            sendPlugins(sender);
            return true;
        }
        if ("task".equals(action)) {
            sendTask(sender,args);
            return true;
        }
        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("MBB-ExamplePlugin 示例命令：info / image / counter / message / config / admins / plugins / task");
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage("插件："+plugin.getName()+" "+plugin.getVersion()
                +"\nAPI："+plugin.getServer().getApiVersion()
                +"\n机器人："+plugin.getServer().getBotName()
                +"\n数据目录："+plugin.getDataFolder()
                +"\n配置路径："+plugin.getConfig().getPath());
    }

    private void sendImage(CommandSender sender) {
        List<String> lines = new ArrayList<>();
        lines.add("这是一张由 ImageUtil 渲染的图片。");
        lines.add("机器人名称："+plugin.getServer().getBotName());
        lines.add("API 版本："+plugin.getServer().getApiVersion());
        String title = plugin.getConfig().getString("imageTitle","MBB-ExamplePlugin");
        sender.sendImage(ImageUtil.toBase64Uri(ImageUtil.renderText(title,lines)));
    }

    private void sendCounter(CommandSender sender) {
        String key = "example-counter-"+sender.getUserID();
        int count = 0;
        try {
            String value = plugin.getServer().getStorage().get(plugin,key);
            if (value != null) count = Integer.parseInt(value);
        } catch (Exception ignored) {
        }
        count++;
        plugin.getServer().getStorage().set(plugin,key,String.valueOf(count));
        sender.sendMessage("插件存储示例：你已经触发 "+count+" 次。");
    }

    private void sendMessage(CommandSender sender) {
        JSONArray message = MessageUtil.message(
                MessageUtil.at(sender.getUserID()),
                MessageUtil.text(" 这是组合消息示例。"));
        JSONObject result;
        if (sender.isGroup()) {
            result = plugin.getServer().getOneBotClient().sendGroupMessage(sender.getGroupID(),message);
        } else {
            result = plugin.getServer().getOneBotClient().sendPrivateMessage(sender.getUserID(),message);
        }
        if (result == null || result.getIntValue("retcode") != 0) {
            plugin.getLogger().sendWarn("组合消息发送失败！");
        }
    }

    private void handleConfig(CommandSender sender,String[] args) {
        if (args.length <= 2) {
            sender.sendMessage("配置 exampleText："+plugin.getHelloText()
                    +"\neventLog="+plugin.getConfig().getBoolean("eventLog",false)
                    +"，rawEventLog="+plugin.getConfig().getBoolean("rawEventLog",false));
            return;
        }
        if (!"set".equalsIgnoreCase(args[2])) {
            sender.sendMessage("用法：/example config set <文本>");
            return;
        }
        if (!sender.hasPermission(CommandPermission.BOT_ADMIN)) {
            sender.sendMessage("你没有权限修改示例配置！");
            return;
        }
        String text = joinArgs(args,3);
        if (text.isEmpty()) {
            sender.sendMessage("配置文本不能为空！");
            return;
        }
        plugin.getConfig().set("helloText",text);
        if (plugin.getConfig().save()) {
            sender.sendMessage("示例配置已保存！");
        } else {
            sender.sendMessage("示例配置保存失败，请查看日志！");
        }
    }

    private void sendAdmins(CommandSender sender) {
        if (!sender.hasPermission(CommandPermission.BOT_ADMIN)) {
            sender.sendMessage("你没有权限查看机器人管理员！");
            return;
        }
        List<Long> admins = plugin.getServer().getAdminList();
        if (admins.isEmpty()) {
            sender.sendMessage("当前没有机器人管理员。");
            return;
        }
        sender.sendMessage("机器人管理员："+joinLongs(admins));
    }

    private void sendPlugins(CommandSender sender) {
        if (!sender.hasPermission(CommandPermission.OWNER)) {
            sender.sendMessage("你没有权限查看插件列表！");
            return;
        }
        List<PluginInfo> plugins = plugin.getServer().getPluginInfoList();
        StringBuilder builder = new StringBuilder("插件列表：");
        for (PluginInfo info : plugins) {
            builder.append("\n").append(info.name).append(" ").append(info.version)
                    .append(info.enabled ? " [启用]" : " [停用]");
        }
        sender.sendMessage(builder.toString());
    }

    private void sendTask(CommandSender sender,String[] args) {
        if (!sender.hasPermission(CommandPermission.BOT_ADMIN)) {
            sender.sendMessage("你没有权限使用延迟任务示例！");
            return;
        }
        int seconds = plugin.getConfig().getInt("taskDelaySeconds",3);
        if (args.length > 2) {
            try {
                seconds = Integer.parseInt(args[2]);
            } catch (Exception ignored) {
            }
        }
        if (seconds < 1) seconds = 1;
        final int delay = seconds;
        plugin.getServer().getPluginManager().runTaskLater(plugin,() ->
                sender.sendMessage("延迟任务已执行，等待时间："+delay+" 秒。"),delay);
        sender.sendMessage("已登记延迟任务，将在 "+seconds+" 秒后发送消息。");
    }

    private String joinArgs(String[] args,int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (builder.length() > 0) builder.append(" ");
            builder.append(args[i]);
        }
        return builder.toString().trim();
    }

    private String joinLongs(List<Long> values) {
        StringBuilder builder = new StringBuilder();
        for (Long value : values) {
            if (builder.length() > 0) builder.append(",");
            builder.append(value);
        }
        return builder.toString();
    }
}
