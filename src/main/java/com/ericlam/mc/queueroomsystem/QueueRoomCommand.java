package com.ericlam.mc.queueroomsystem;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.DefaultCommand;

public class QueueRoomCommand extends DefaultCommand {

    public QueueRoomCommand(QueueRoomConfig config, QueueRoomMessage message, QueueRoomManager manager) {
        super(null, "queue", null, "隊列系統指令");
        this.addSub(new JoinQueueRoomCommand(this, config, message, manager));
        this.addSub(new LeaveQueueRoomCommand(this, config, message, manager));
    }
}
