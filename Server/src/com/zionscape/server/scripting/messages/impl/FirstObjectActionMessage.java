package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.Location;

/**
 * @author Stuart
 */
public final class FirstObjectActionMessage extends ObjectActionMessage {

	public FirstObjectActionMessage(int objectId, Location location) {
		super(objectId, location, 1);
	}

}