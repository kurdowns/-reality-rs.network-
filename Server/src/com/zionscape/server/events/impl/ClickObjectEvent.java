package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;

public class ClickObjectEvent extends Event {

    private final Player player;
    private int objectId;
    private Location objectLocation;
    private int option;

    public ClickObjectEvent(Player player, int objectId, Location objectLocation, int option) {
        this.player = player;
        this.objectId = objectId;
        this.objectLocation = objectLocation;
        this.option = option;
    }

    public Player getPlayer() {
        return player;
    }

    public int getObjectId() {
        return objectId;
    }

    public Location getObjectLocation() {
        return objectLocation;
    }

    public int getOption() {
        return option;
    }

}
