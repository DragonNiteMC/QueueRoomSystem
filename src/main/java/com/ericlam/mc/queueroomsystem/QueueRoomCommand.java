package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.DefaultCommand;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.MessageFormat;
import java.util.List;

public class QueueRoomCommand extends DefaultCommand {

    public QueueRoomCommand(QueueRoomConfig config, QueueRoomMessage message, QueueRoomManager manager) {
        super(null, "queue", null, "隊列系統指令");
        this.addSub(new JoinQueueRoomCommand(this, config, message, manager));
        this.addSub(new LeaveQueueRoomCommand(this, config, message, manager));
    }
}
