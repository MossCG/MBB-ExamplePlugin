package org.moboxlab.mbb.example;

import com.alibaba.fastjson.JSONArray;
import org.moboxlab.moboxbot.API.Event.EventHandler;
import org.moboxlab.moboxbot.API.Event.EventPriority;
import org.moboxlab.moboxbot.API.Event.GroupMessageEvent;
import org.moboxlab.moboxbot.API.Event.Listener;
import org.moboxlab.moboxbot.API.Event.MetaEvent;
import org.moboxlab.moboxbot.API.Event.NoticeEvent;
import org.moboxlab.moboxbot.API.Event.PrivateMessageEvent;
import org.moboxlab.moboxbot.API.Event.RawEvent;
import org.moboxlab.moboxbot.API.Event.RequestEvent;
import org.moboxlab.moboxbot.API.OneBot.MessageUtil;

/**
 * 事件监听示例
 */
public class ExampleListener implements Listener {
    private final ExamplePlugin plugin;

    public ExampleListener(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onGroupMessage(GroupMessageEvent event) {
        log("收到群消息："+event.getGroupID()+" / "+event.getUserID()+" / "+event.getRawMessage(),event.getRaw().toJSONString());
        if ("example-reply".equals(event.getRawMessage())) {
            JSONArray message = MessageUtil.message(
                    MessageUtil.reply(event.getMessageID()),
                    MessageUtil.text("这是 MBB-ExamplePlugin 的回复消息段示例。"));
            plugin.getServer().getOneBotClient().sendGroupMessage(event.getGroupID(),message);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrivateMessage(PrivateMessageEvent event) {
        log("收到私聊消息："+event.getUserID()+" / "+event.getRawMessage(),event.getRaw().toJSONString());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNotice(NoticeEvent event) {
        log("收到通知："+event.getNoticeType()+"/"+event.getSubType()
                +" 用户="+event.getUserID()+" 群="+event.getGroupID(),event.getRaw().toJSONString());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRequest(RequestEvent event) {
        log("收到请求："+event.getRequestType()+"/"+event.getSubType()
                +" 用户="+event.getUserID()+" 群="+event.getGroupID()
                +" 备注="+event.getComment(),event.getRaw().toJSONString());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMeta(MetaEvent event) {
        if ("heartbeat".equals(event.getMetaEventType())) return;
        log("收到元事件："+event.getMetaEventType()+"/"+event.getSubType(),event.getRaw().toJSONString());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRaw(RawEvent event) {
        log("收到未识别事件。",event.getRaw().toJSONString());
    }

    private void log(String message,String raw) {
        if (!plugin.getConfig().getBoolean("eventLog",false)) return;
        plugin.getLogger().sendInfo(message);
        if (plugin.getConfig().getBoolean("rawEventLog",false)) {
            plugin.getLogger().sendInfo("原始事件："+raw);
        }
    }
}
