package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.items.Item;

/**
 * @author stuart
 */
public final class FirstItemActionMessage extends ItemActionMessage {

	public FirstItemActionMessage(Item item, int slot) {
		super(item, slot, 1);
	}

}