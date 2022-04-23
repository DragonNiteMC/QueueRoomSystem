package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.config.yaml.BungeeConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

import java.util.List;
import java.util.Map;

@Resource(locate = "config.yml")
public class QueueRoomConfig extends BungeeConfiguration {


    public Map<String, QueueSettings> servers;

    public static class QueueSettings {

        public List<String> rooms;
        public int leastPlayers;
        public int maxPlayers;

    }
}
