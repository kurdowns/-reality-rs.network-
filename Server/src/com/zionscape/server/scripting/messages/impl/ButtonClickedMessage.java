package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.scripting.messages.Message;

/**
 * @author stuart
 */
public final class ButtonClickedMessage extends Message {

	private final int id;

	public ButtonClickedMessage(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}