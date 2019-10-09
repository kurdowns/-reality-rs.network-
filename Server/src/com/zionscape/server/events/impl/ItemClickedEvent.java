package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.players.Player;

public class ItemClickedEvent extends Event {

    private final Player player;
    private final int itemId;
    private final int option;

    public ItemClickedEvent(Player player, int itemId, int option) {
        this.player = player;
        this.itemId = itemId;
        this.option = option;
    }

    public Player getPlayer() {
        return player;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOption() {
        return option;
    }

}
