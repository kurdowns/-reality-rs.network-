package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.players.Player;

public class PlayerProcessEvent extends Event {

    private final Player player;

    public PlayerProcessEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
