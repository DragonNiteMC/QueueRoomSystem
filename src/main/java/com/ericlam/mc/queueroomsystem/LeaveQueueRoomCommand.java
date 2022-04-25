package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class LeaveQueueRoomCommand extends CommandNode {

    private final QueueRoomConfig config;
    private final QueueRoomMessage message;
    private final QueueRoomManager manager;

    public LeaveQueueRoomCommand(CommandNode parent, QueueRoomConfig config, QueueRoomMessage message, QueueRoomManager manager) {
        super(parent, "leave", null, "離開房間隊列", null);
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
        if (!manager.isInQueue(name, player)) {
            MessageBuilder.sendMessage(player, message.get("not-queued"));
            return;
        }
        manager.removeQueue(player);
        MessageBuilder.sendMessage(player, message.get("leave-queue-success"));
    }

    @Override
    public List<String> executeTabCompletion(CommandSender commandSender, List<String> list) {
        return null;
    }
}
