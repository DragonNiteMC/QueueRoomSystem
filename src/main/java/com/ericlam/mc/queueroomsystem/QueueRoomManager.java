package com.ericlam.mc.queueroomsystem;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class QueueRoomManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueRoomManager.class);
    private final QueueRoomEvent event;
    private final Plugin plugin;

    private final Map<String, Queue<ProxiedPlayer>> queueMap = new ConcurrentHashMap<>();
    private final Map<String, ScheduledTask> taskMap = new ConcurrentHashMap<>();


    public QueueRoomManager(QueueRoomEvent event, Plugin plugin) {
        this.event = event;
        this.plugin = plugin;
    }

    public void addQueue(String server, ProxiedPlayer player) {
        if (event.beforeAddPlayer(server, player)) {
            if (!queueMap.containsKey(server)) {
                queueMap.put(server, new ArrayDeque<>());
            }
            queueMap.get(server).offer(player);
            LOGGER.info("玩家 {} 已加入 {} 的隊列 ({} 個)", player.getName(), server, queueMap.get(server).size());
            event.onAddedQueue(server, queueMap.get(server));
            if (taskMap.containsKey(server)) return;
            var task = ProxyServer.getInstance().getScheduler().schedule(plugin, () -> event.onAddedQueue(server, queueMap.get(server)), 1, 1, TimeUnit.MINUTES);
            this.taskMap.put(server, task);
            LOGGER.info("已啟動 {} 的隊列任務 (每一分鐘檢查隊列)", server);
        }
    }

    public void removeQueue(ProxiedPlayer player) {
        queueMap.forEach((k, l) -> {
            if (l.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()))) {
                LOGGER.info("玩家 {} 已離開 {} 的隊列 ({} 個)", player.getName(), k, l.size());
                event.onRemoveQueue(k, player);
            }
            if (l.size() > 0) return;
            var task = taskMap.remove(k);
            if (task == null) return;
            task.cancel();
            LOGGER.info("已停止 {} 的隊列任務", k);
        });
    }

    public void clearQueue(String server) {
        if (queueMap.containsKey(server)) {
            queueMap.remove(server);
            event.onClearQueue(server);
        }
    }


    public Queue<ProxiedPlayer> getQueue(String server) {
        return queueMap.get(server);
    }

    public boolean isInQueue(String server, ProxiedPlayer player) {
        return queueMap.containsKey(server) && queueMap.get(server).contains(player);
    }

    public boolean isInQueue(String server) {
        return queueMap.containsKey(server);
    }

}
