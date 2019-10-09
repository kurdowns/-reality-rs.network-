package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;

public class ItemOnObjectEvent extends Event {

    private final Player player;
    private final int item;
    private final int objectId;
    private final Location objectLocation;

    public ItemOnObjectEvent(Player player, int item, int objectId, Location objectLocation) {
        this.player = player;
        this.item = item;
        this.objectId = objectId;
        this.objectLocation = objectLocation;
    }

    public Player getPlayer() {
        return player;
    }

    public int getItem() {
        return item;
    }

    public int getObjectId() {
        return objectId;
    }

    public Location getObjectLocation() {
        return objectLocation;
    }

}
