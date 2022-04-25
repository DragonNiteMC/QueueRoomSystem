package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Queue;

public record QueueRoomEventBus(QueueRoomConfig config) implements QueueRoomEvent {

    private static final Logger BUS_LOGGER = LoggerFactory.getLogger(QueueRoomEventBus.class);

    @Override
    public void onAddedQueue(String server, Queue<ProxiedPlayer> proxiedPlayers) {
        QueueRoomConfig.QueueSettings settings = config.servers.get(server);
        if (settings == null) return; // 保險
        if (proxiedPlayers.size() >= settings.leastPlayers) {
            try (Jedis jedis = DragonNiteMC.getAPI().getRedisDataSource().getJedis()) {

                for (String room : settings.rooms) {

                    ServerInfo info = ProxyServer.getInstance().getServerInfo(room);
                    if (info == null) {
                        BUS_LOGGER.warn("伺服器房間 {} 無效", room);
                        continue;
                    }

                    String state = jedis.hget(config.getRedisKey, room);
                    BUS_LOGGER.warn("State：" + state);
                    if (state == null) continue;
                    boolean available = state.equalsIgnoreCase(config.availableState);

                    if (info.getPlayers().size() < settings.maxPlayers && available) {
                        int total = proxiedPlayers.size();
                        int remain = sendServer(info, proxiedPlayers, settings.maxPlayers);
                        BUS_LOGGER.info("成功發送 {} 個玩家到房間 {}，剩餘 {} 個", total - remain, room, remain);

                        if (remain < settings.leastPlayers) break;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                BUS_LOGGER.error("讀取 redis 時發生錯誤", e);
            }

        }
    }

    @Override
    public void onRemoveQueue(String server, ProxiedPlayer removed) {

    }

    @Override
    public void onClearQueue(String server) {

    }

    @Override
    public boolean beforeAddPlayer(String server, ProxiedPlayer proxiedPlayer) {

        boolean available = config.servers.containsKey(server);

        if (!available) {
            BUS_LOGGER.warn("玩家 {} 嘗試從伺服器 {} 加入隊列, 但該隊列沒有在 config 中記錄。", proxiedPlayer.getName(), server);
        }

        return available;
    }

    private int sendServer(ServerInfo server, Queue<ProxiedPlayer> players, int maxPlayers) {
        while (!players.isEmpty() && (maxPlayers - server.getPlayers().size()) > players.size()) {
            ProxiedPlayer player = players.poll();
            if (player != null) {
                player.connect(server);
            }
        }
        return players.size();
    }
}
