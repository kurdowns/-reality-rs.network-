package com.zionscape.server.world.shops;

import java.util.ArrayList;
import java.util.List;

public class Shop {

	public static final int INVENTORY_INTERFACE_ID = 3214;
	private final int id;
	private final String name;
	private final Currency currency;
	private final boolean generalStore;
	private final boolean buysItems;
	private final List<ShopItem> items = new ArrayList<>();
	private final int replenishTime;
	private final boolean buyBackFullPrice;

	public Shop(int id, String name, Currency currency, boolean generalStore, boolean buysItems, int replenishTime, boolean buyBackFullPrice) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.generalStore = generalStore;
		this.buysItems = buysItems;
		this.replenishTime = replenishTime;
		this.buyBackFullPrice = buyBackFullPrice;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Currency getCurrency() {
		return currency;
	}

	public boolean isGeneralStore() {
		return generalStore;
	}

	public boolean buysItems() {
		return buysItems;
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public int getReplenishTime() {
		return replenishTime;
	}

	public boolean buyBackFullPrice() {
		return buyBackFullPrice;
	}
}