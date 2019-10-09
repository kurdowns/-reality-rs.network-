package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.Location;
import com.zionscape.server.scripting.messages.Message;

/**
 * @author Stuart
 */
public abstract class ObjectActionMessage extends Message {

	private final int objectId;
	private final Location location;
	private final int option;

	public ObjectActionMessage(int objectId, Location location, int option) {
		this.objectId = objectId;
		this.location = location;
		this.option = option;
	}

	public int getObjectId() {
		return objectId;
	}

	public Location getLocation() {
		return location;
	}

	public int getOption() {
		return option;
	}

}