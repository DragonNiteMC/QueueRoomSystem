package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.config.YamlManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class QueueRoomManager {

    private final QueueRoomEvent event;

    private final Map<String, Queue<ProxiedPlayer>> queueMap = new ConcurrentHashMap<>();

    public QueueRoomManager(QueueRoomEvent event) {
        this.event = event;
    }

    public void addQueue(String server, ProxiedPlayer player) {
        if (event.beforeAddPlayer(server, player)) {
            if (!queueMap.containsKey(server)) {
                queueMap.put(server, new ConcurrentLinkedDeque<>());
            }
            queueMap.get(server).offer(player);
            event.onAddedQueue(server, queueMap.get(server));
        }
    }

    public void removeQueue(ProxiedPlayer player) {
        queueMap.forEach((k, l) -> {
            if (l.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()))){
                event.onRemoveQueue(k, player);
            }
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
