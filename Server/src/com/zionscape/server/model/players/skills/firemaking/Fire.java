package com.zionscape.server.model.players.skills.firemaking;

import com.zionscape.server.model.Location;

public class Fire {

    private final Location location;
    private int using;

    public Fire(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public int getUsing() {
        return using;
    }

    public void setUsing(int using) {
        this.using = using;
    }
}
