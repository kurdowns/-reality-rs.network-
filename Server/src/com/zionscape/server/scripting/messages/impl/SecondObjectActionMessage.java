package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.Location;

/**
 * @author Stuart
 */
public final class SecondObjectActionMessage extends ObjectActionMessage {

	public SecondObjectActionMessage(int objectId, Location location) {
		super(objectId, location, 2);
	}

}