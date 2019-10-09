package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.items.Item;
import com.zionscape.server.scripting.messages.Message;

/**
 * @author Stuart
 */
public abstract class ItemActionMessage extends Message {

	private final Item item;
	private final int slot;
	private final int option;

	public ItemActionMessage(Item item, int slot, int option) {
		this.item = item;
		this.slot = slot;
		this.option = option;
	}

	public Item getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public int getOption() {
		return option;
	}

}