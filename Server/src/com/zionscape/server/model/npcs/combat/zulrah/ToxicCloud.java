package com.zionscape.server.model.npcs.combat.zulrah;

import com.zionscape.server.model.Location;

public class ToxicCloud {

	private final Location location;
	private boolean active;

	public ToxicCloud(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
