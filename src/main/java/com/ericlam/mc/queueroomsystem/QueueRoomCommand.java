package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class QueueRoomCommand extends CommandNode {

    public QueueRoomCommand() {
        super(null, "queue", null, "加入房間隊列", null);
    }

    @Override
    public void executeCommand(CommandSender commandSender, List<String> list) {

    }

    @Override
    public List<String> executeTabCompletion(CommandSender commandSender, List<String> list) {
        return null;
    }
}
