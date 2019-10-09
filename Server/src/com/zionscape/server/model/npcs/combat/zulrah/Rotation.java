package com.zionscape.server.model.npcs.combat.zulrah;

import com.zionscape.server.model.Location;

public class Rotation {

	private final Location location;
	private final Type type;

	public Rotation(Location location, Type type) {
		this.location = location;
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public Location getLocation() {
		return location;
	}

}