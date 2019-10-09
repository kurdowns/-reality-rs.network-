package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.scripting.messages.Message;

/**
 * @author Stuart
 */
public final class DialogueOptionClickedMessage extends Message {

	private final int option;

	public DialogueOptionClickedMessage(int option) {
		this.option = option;
	}

	public int getOption() {
		return option;
	}

}