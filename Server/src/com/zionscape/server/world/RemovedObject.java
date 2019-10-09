package com.zionscape.server.world;

import com.zionscape.server.model.Location;

public class RemovedObject {

	private final Location location;
	private final int type;

	public RemovedObject(Location location, int type) {
		this.location = location;
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public int getType() {
		return type;
	}

}
