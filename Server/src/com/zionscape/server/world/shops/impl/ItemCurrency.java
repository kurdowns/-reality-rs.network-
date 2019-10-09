package com.zionscape.server.world.shops.impl;


import com.zionscape.server.model.items.ItemAssistant;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Currency;

public final class ItemCurrency implements Currency {

	private final int itemId;

	public ItemCurrency(String arg) {
		this.itemId = Integer.parseInt(arg);
	}

	@Override
	public String getName() {
		return ItemUtility.getName(itemId);
	}

	@Override
	public void add(Player client, int amount) {
		client.getItems().addItem(itemId, amount);
	}

	@Override
	public int has(Player client) {
		return client.getItems().getItemAmount(itemId);
	}

	@Override
	public void remove(Player client, int amount) {
		client.getItems().deleteItem(itemId, amount);
	}

	@Override
	public int sellValue(int itemId, boolean fullPrice) {

		if (fullPrice) {
			return ItemAssistant.getItemValue(itemId);
		}

		return (int) Math.floor(ItemAssistant.getItemValue(itemId) * 0.75);
	}

	@Override
	public int buyValue(int itemId) {
		return ItemAssistant.getItemValue(itemId);
	}

}