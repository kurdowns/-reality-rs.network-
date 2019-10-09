package com.zionscape.server.world.shops;

import java.util.ArrayList;
import java.util.List;

public class JsonShop {

	private int id;
	private String name;
	private String currency;
	private boolean generalStore;
	private boolean buysItems = true;
	private int replenishTime;
	private List<JsonItem> items = new ArrayList<>();
	private boolean buyBackFullPrice = false;

	public JsonShop(int id, String name, String currency, int replenishTime) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.replenishTime = replenishTime;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCurrency() {
		return currency;
	}

	public List<JsonItem> getItems() {
		return items;
	}

	public boolean generalStore() {
		return generalStore;
	}

	public boolean buysItems() {
		return buysItems;
	}

	public void setBuysItems(boolean buysItems) {
		this.buysItems = buysItems;
	}

	public void setGeneralStore(boolean generalStore) {
		this.generalStore = generalStore;
	}

	public int getReplenishTime() {
		return replenishTime;
	}

	public boolean isBuyBackFullPrice() {
		return buyBackFullPrice;
	}
}