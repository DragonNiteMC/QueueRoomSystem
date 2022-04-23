package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.config.YamlManager;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class QueueRoomSystem extends Plugin implements Listener {

    private YamlManager yamlManager;
    private QueueRoomManager queueRoomManager;

    private QueueRoomConfig config;

    @Override
    public void onEnable() {
        super.onEnable();
        DragonNiteMC.getAPI().getCommandRegister().registerCommand(this, new QueueRoomCommand());
        yamlManager = DragonNiteMC.getAPI().getConfigFactory(this)
                .register(QueueRoomConfig.class)
                .dump();
        config = yamlManager.getConfigAs(QueueRoomConfig.class);
        queueRoomManager = new QueueRoomManager(new QueueRoomEventBus(config));
        getProxy().getPluginManager().registerListener(this, this);
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }


    public YamlManager getYamlManager() {
        return yamlManager;
    }

    public QueueRoomManager getQueueRoomManager() {
        return queueRoomManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSwitch(ServerConnectEvent e){
        // 在 queueing 的伺服器中
        if (config.servers.containsKey(e.getPlayer().getServer().getInfo().getName())) {
            queueRoomManager.removeQueue(e.getPlayer());
        }
    }
}
