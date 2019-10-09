package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.Location;

/**
 * @author Stuart
 */
public final class ThirdObjectActionMessage extends ObjectActionMessage {

	public ThirdObjectActionMessage(int objectId, Location location) {
		super(objectId, location, 3);
	}

}