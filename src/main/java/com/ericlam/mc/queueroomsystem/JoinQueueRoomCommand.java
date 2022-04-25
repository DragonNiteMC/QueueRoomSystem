package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.MessageFormat;
import java.util.List;

public class JoinQueueRoomCommand extends CommandNode {

    private final QueueRoomConfig config;
    private final QueueRoomMessage message;
    private final QueueRoomManager manager;

    public JoinQueueRoomCommand(CommandNode parent, QueueRoomConfig config, QueueRoomMessage message, QueueRoomManager manager) {
        super(parent, "join", null, "加入房間隊列", null);
        this.config = config;
        this.message = message;
        this.manager = manager;
    }

    @Override
    public void executeCommand(CommandSender commandSender, List<String> list) {
        if (!(commandSender instanceof ProxiedPlayer)) return;
        var player = (ProxiedPlayer) commandSender;
        String name = player.getServer().getInfo().getName();
        if (!config.servers.containsKey(name)) {
            MessageBuilder.sendMessage(player, message.get("not-available"));
            return;
        }
        if (manager.isInQueue(name, player)) {
            MessageBuilder.sendMessage(player, message.get("queued"));
            return;
        }
        manager.addQueue(name, player);
        var queue = manager.getQueue(name).size();
        var max = config.servers.get(name).leastPlayers;
        MessageBuilder.sendMessage(player, MessageFormat.format(message.get("queue-success"), queue, max));
    }

    @Override
    public List<String> executeTabCompletion(CommandSender commandSender, List<String> list) {
        return null;
    }

}
