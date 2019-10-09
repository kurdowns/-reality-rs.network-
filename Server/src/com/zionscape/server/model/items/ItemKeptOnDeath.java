package com.zionscape.server.model.items;

public class ItemKeptOnDeath {

	private final int amount;
	private int item;
	private int cost;

	public ItemKeptOnDeath(final int item, final int amount, final int cost) {
		this.item = item;
		this.amount = amount;
		this.cost = cost;
	}

	public int getId() {
		return item;
	}

	public int getAmount() {
		return amount;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setItem(int item) {
		this.item = item;
	}

}
