package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.players.Player;

public class ClickingButtonEvent extends Event {

    private final Player player;
    private final int button;

    public ClickingButtonEvent(Player player, int button) {
        this.player = player;
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public Player getPlayer() {
        return player;
    }

}
