package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.scripting.messages.Message;

/**
 * @author Stuart
 */
public final class ItemOnObjectMessage extends Message {

	private final int objectId;
	private final Location objectLocation;
	private final Item item;
	private final int itemSlot;

	public ItemOnObjectMessage(int objectId, Location objectLocation, Item item, int itemSlot) {
		this.objectId = objectId;
		this.objectLocation = objectLocation;
		this.item = item;
		this.itemSlot = itemSlot;
	}

	public int getObjectId() {
		return objectId;
	}

	public Location getObjectLocation() {
		return objectLocation;
	}

	public Item getItem() {
		return item;
	}

	public int getItemSlot() {
		return itemSlot;
	}
}
