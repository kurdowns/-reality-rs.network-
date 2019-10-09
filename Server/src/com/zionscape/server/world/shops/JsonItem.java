package com.zionscape.server.world.shops;

public class JsonItem {

	private final int id;
	private final int amount;
	private int price;

	public JsonItem(int id, int amount, int price) {
		this.id = id;
		this.amount = amount;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
