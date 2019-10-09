package com.zionscape.server.model.items;

public class GameItem {

	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id, int amount) {
		if (ItemUtility.isStackable(id)) {
			stackable = true;
		}
		this.id = id;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return ItemUtility.getName(id) + "(" + id + ") - " + amount;
	}

}