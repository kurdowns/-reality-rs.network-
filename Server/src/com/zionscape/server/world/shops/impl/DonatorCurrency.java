package com.zionscape.server.world.shops.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Currency;

public final class DonatorCurrency implements Currency {

	@Override
	public String getName() {
		return "Donator Points";
	}

	@Override
	public void add(Player client, int amount) {
		client.donatorPoint += amount;
	}

	@Override
	public int has(Player client) {
		return client.donatorPoint;
	}

	@Override
	public void remove(Player client, int amount) {
		client.donatorPoint -= amount;
	}

	@Override
	public int sellValue(int itemId, boolean fullPrice) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int buyValue(int itemId) {
		throw new UnsupportedOperationException();
	}

}