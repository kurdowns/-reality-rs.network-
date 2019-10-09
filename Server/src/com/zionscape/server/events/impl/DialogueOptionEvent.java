package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.players.Player;

public class DialogueOptionEvent extends Event {

    private final Player player;
    private final int option;

    public DialogueOptionEvent(Player player, int option) {
        this.player = player;
        this.option = option;
    }

    public Player getPlayer() {
        return player;
    }

    public int getOption() {
        return option;
    }

}
