package com.ericlam.mc.queueroomsystem;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Queue;

public interface QueueRoomEvent {

    void onAddedQueue(String server, Queue<ProxiedPlayer> proxiedPlayers);

    void onRemoveQueue(String server, ProxiedPlayer removed);

    void onClearQueue(String server);

    boolean beforeAddPlayer(String server, ProxiedPlayer proxiedPlayer);

}
